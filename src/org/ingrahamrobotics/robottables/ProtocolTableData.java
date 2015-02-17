package org.ingrahamrobotics.robottables;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.ingrahamrobotics.robottables.interfaces.RobotProtocol;
import org.ingrahamrobotics.robottables.util.UpdateableDelayedRunnable;

public class ProtocolTableData {

    private final Object tableUpdateLock = new Object();
    private final InternalTable internalTable;
    private final RobotProtocol protocolHandler;
    private UpdateState userUpdateState = UpdateState.COMPLETE;
    private int currentExpectedUserKeys;
    private Set<String> currentUserUpdatedKeys;
    private UpdateState adminUpdateState = UpdateState.COMPLETE;
    private int currentExpectedAdminKeys;
    private Set<String> currentAdminUpdatedKeys;
    /**
     * If we've already received the UPDATE END message for the current update, we shouldn't keep extending the
     * tableUpdateTimeout timeout.
     */
    private boolean endMessageReceived;
    /**
     * Whether this table is confirmed to be owned by us.
     */
    private boolean readyToPublish;
    /**
     * Runnable that sends full updates
     */
    private UpdateableDelayedRunnable fullUpdateRunnable;

    /**
     * Runnable that handles timeouts for the table.
     */
    private UpdateableDelayedRunnable tableUpdateTimeout;

    public ProtocolTableData(final RobotProtocol protocolHandler, InternalTable internalTable) {
        this.protocolHandler = protocolHandler;
        this.internalTable = internalTable;

        fullUpdateRunnable = new UpdateableDelayedRunnable(new Runnable() {
            public void run() {
                ProtocolTableData.this.protocolHandler.sendFullUpdate(ProtocolTableData.this.internalTable);
            }
        });
        tableUpdateTimeout = new UpdateableDelayedRunnable(new Runnable() {
            public void run() {
                synchronized (tableUpdateLock) {
                    if (userUpdateState == UpdateState.RUNNING || adminUpdateState == UpdateState.RUNNING) {
                        resetUpdateValues();
                    }
                }
            }
        });
    }

    public boolean isReadyToPublish() {
        return readyToPublish;
    }

    public void setReadyToPublish(final boolean readyToPublish) {
        this.readyToPublish = readyToPublish;
    }

    public UpdateableDelayedRunnable getFullUpdateRunnable() {
        return fullUpdateRunnable;
    }

    public void userUpdateStarted(int expectedUserKeys) {
        synchronized (tableUpdateLock) {
            if (this.userUpdateState != UpdateState.COMPLETE) {
                this.resetUpdateValues();
            }
            this.currentUserUpdatedKeys = new HashSet<String>(expectedUserKeys);
            this.currentExpectedUserKeys = expectedUserKeys;
            if (expectedUserKeys <= 0) {
                // If we don't expect any keys, then this part is already complete
                this.userUpdateState = UpdateState.ENDED_WAITING;
                if (adminUpdateState == UpdateState.ENDED_WAITING) {
                    updateEndedSuccessfully();
                }
            } else {
                this.userUpdateState = UpdateState.RUNNING;
            }
            // There is a posibility that we are already done at this point, so check just to be sure.
            if (userUpdateState != UpdateState.COMPLETE) {
                tableUpdateTimeout.delayUntil(System.currentTimeMillis() + TimeConstants.MAX_INTERVAL_DURING_UPDATE);
            }
        }
    }

    public void adminUpdateStarted(int expectedAdminKeys) {
        synchronized (tableUpdateLock) {
            if (this.adminUpdateState != UpdateState.COMPLETE) {
                resetUpdateValues();
            }
            this.currentAdminUpdatedKeys = new HashSet<String>(expectedAdminKeys);
            this.currentExpectedAdminKeys = expectedAdminKeys;
            if (expectedAdminKeys <= 0) {
                // If we don't expect any keys, then this part is already complete
                this.adminUpdateState = UpdateState.ENDED_WAITING;
                if (userUpdateState == UpdateState.ENDED_WAITING) {
                    updateEndedSuccessfully();
                }
            } else {
                this.adminUpdateState = UpdateState.RUNNING;
            }
            // There is a posibility that we are already done at this point, so check just to be sure.
            if (adminUpdateState != UpdateState.COMPLETE) {
                tableUpdateTimeout.delayUntil(System.currentTimeMillis() + TimeConstants.MAX_INTERVAL_DURING_UPDATE);
            }
        }
    }

    public void internalUserUpdated(String key) {
        synchronized (tableUpdateLock) {
            if (userUpdateState == UpdateState.RUNNING) {
                // If we have an incomplete update
                currentUserUpdatedKeys.add(key);
                if (currentUserUpdatedKeys.size() >= currentExpectedUserKeys) {
                    userUpdateState = UpdateState.ENDED_WAITING;
                    if (adminUpdateState == UpdateState.ENDED_WAITING) {
                        updateEndedSuccessfully();
                    }
                }
                if (userUpdateState != UpdateState.COMPLETE && !endMessageReceived) {
                    // If the update hasn't ended yet (if we haven't called updateEndedSuccessfully() above)
                    tableUpdateTimeout.delayUntil(System.currentTimeMillis() + TimeConstants.MAX_INTERVAL_DURING_UPDATE);
                }
            }
        }
    }

    public void internalAdminUpdated(String key) {
        synchronized (tableUpdateLock) {
            if (adminUpdateState == UpdateState.RUNNING) {
                // If we have an incomplete update
                currentAdminUpdatedKeys.add(key);
                if (currentAdminUpdatedKeys.size() >= currentExpectedAdminKeys) {
                    adminUpdateState = UpdateState.ENDED_WAITING;
                    if (userUpdateState == UpdateState.ENDED_WAITING) {
                        updateEndedSuccessfully();
                    }
                }
                if (adminUpdateState != UpdateState.COMPLETE && !endMessageReceived) {
                    // If the update hasn't ended yet (if we haven't called updateEndedSuccessfully() above)
                    tableUpdateTimeout.delayUntil(System.currentTimeMillis() + TimeConstants.MAX_INTERVAL_DURING_UPDATE);
                }
            }
        }
    }

    public void endMessageReceived(int totalCountExpected) {
        // TODO: What should we do with totalCountExpected here?
        synchronized (tableUpdateLock) {
            // We should just continue counting updates for another 100 ms;
            if (adminUpdateState == UpdateState.RUNNING || userUpdateState == UpdateState.RUNNING) {
                endMessageReceived = true;
                tableUpdateTimeout.delayUntil(System.currentTimeMillis() + TimeConstants.MAX_INTERVAL_DURING_UPDATE);
            }
        }
    }

    private void updateEndedSuccessfully() {
        Set<String> updatedUserKeys;
        Set<String> updatedAdminKeys;
        synchronized (tableUpdateLock) {
            // Copy references to these sets, because the instance variables are set to null in resetUpdateValues()
            updatedUserKeys = currentUserUpdatedKeys;
            updatedAdminKeys = currentAdminUpdatedKeys;
            // Since endedUnsuccessfully() doesn't do anything besides reset stuff, we can just call it in order to not duplicate code.
            resetUpdateValues();
        }

        // Remove user keys not in update
        if (updatedUserKeys.isEmpty()) {
            internalTable.internalClear();
        } else {
            Set<String> keySetCopy = new TreeSet<String>(internalTable.getKeySet());
            keySetCopy.removeAll(updatedUserKeys);
            for (String removed : keySetCopy) {
                internalTable.internalSet(removed, null);
            }
        }

        // Remove admin keys not in update.
        // We don't have an .isEmpty() shortcut for admin because it should never be the case that there are no admin keys.
        // (UPDATE_INTERVAL and GENERATION_COUNT should always exist)

        Set<String> adminKeySetCopy = new TreeSet<String>(internalTable.getAdminKeySet());
        adminKeySetCopy.removeAll(updatedAdminKeys);
        for (String removed : adminKeySetCopy) {
            internalTable.internalSetAdmin(removed, null);
        }

        // Successfully updated!
        internalTable.updatedNow();

        // Send successful update confirmation
        protocolHandler.sendFullUpdateSuccessConfirmation(internalTable);
    }

    /**
     * Called to reset update values, after a successful or unsuccessful update.
     */
    private void resetUpdateValues() {
        synchronized (tableUpdateLock) {
            // Reset things
            endMessageReceived = false;
            currentExpectedUserKeys = -1;
            currentExpectedAdminKeys = -1;
            currentUserUpdatedKeys = null;
            currentAdminUpdatedKeys = null;
            userUpdateState = UpdateState.COMPLETE;
            adminUpdateState = UpdateState.COMPLETE;
        }
    }

    private static enum UpdateState {
        /**
         * Currently running, accepting new keys.
         */
        RUNNING,
        /**
         * Waiting for the other update part to complete (admin update waiting for user update, or vice versa)
         */
        ENDED_WAITING,
        /**
         * Update complete, updateEndedSuccessfully() or resetUpdateValues() has been fired since this update started
         * and finished.
         */
        COMPLETE,
    }
}
