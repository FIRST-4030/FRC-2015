package org.ingrahamrobotics.robot2015.output;

import java.util.HashMap;
import java.util.Map;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;
import org.ingrahamrobotics.robottables.api.TableType;
import org.ingrahamrobotics.robottables.api.UpdateAction;
import org.ingrahamrobotics.robottables.api.listeners.ClientUpdateListener;
import org.ingrahamrobotics.robottables.api.listeners.TableUpdateListener;

/**
 * Class for getting driver input settings from the driver station.
 */
public class Settings implements ClientUpdateListener, TableUpdateListener {

    /**
     * Possible keys for input settings
     */
    public static enum Key {
        INDEXER_LEVEL_MAX_WAIT_TIME("Indexer one-up max time", "10000", false),
        INDEXER_LEVEL_USE_ENCODER("Indexer one-up use encoder?", "y", false),
        INDEXER_LEVEL_ENCODER_TICKS("Indexer one-up encoder ticks", "700", false),
        INDEXER_FIXED_SPEED("Indexer collapse/shift speed", "1", false),
        INDEXER_MAX_HEIGHT("Indexer max height", "6800"),
        TOTE_CLEARANCE_ADDITION("Indexer tote clearance addition", "500", false),
        INDEXER_INITIAL_CLEARANCE_UP("Indexer initial upwards movement", "200", false),
        TOTE_INTAKE_IN_SPEED("Tote intake in-speed", "1", false),
        TOTE_INTAKE_OUT_SPEED("Tote intake out-speed", "-1", false),
        STEER_PID_P("Steer PID: P", "3.2"),
        STEER_PID_I("Steer PID: I", "0.8"),
        STEER_PID_D("Steer PID: D", "0"),
        DRIVE_SPEED_MULTIPLIER("drive-speed-multiplier", "0.65"),
        TURN_SPEED_MULTIPLIER("turn-speed-multiplier", "0.1"),
        TURNING_SLOP("slop in gearbox (radians)", String.valueOf(Math.PI / 60), false),
        DRIVE_RESET_ENCODERS_ON_ENABLE("Reset swerve encoders on enable", "n", false),
        OLD_AUTO_ROUTINE_TIME("Autonomous time", "1000", false),
        AUTO_WHEEL_ANGLE_ALLOWANCE_BEFORE_RUNNING_MOTORS("Autonomous wheel angle difference allowance before starting motors", "0.2", false),
        AUTO_MS_PER_RADIANS_TURNING("Autonomous ms per radians turning", "1000", false),
        AUTO_MS_PER_FOOT_FORWARD("Autonomous ms per foot forward", "1000", false),
        AUTO_FWD_STR_SPEED("Autonomous strafe/forward speed", "0.5"),
        INDEXER_SPEED("Manual indexer control modifier", "-0.75"),
        AUTO_TURN_SPEED("Autonomous turn speed", "0.2", false),
        AUTO_ROUTINE_FWD("Autonomous forward", "-1.45"), // would be in `ms` once we have ms per radians configured
        AUTO_ROUTINE_STR("Autonomous strafe", "0"), // would be in `ms` once we have ms per radians configured
        DRIVE_WFW_ALLOWANCE("drive-wait-for-wheels-angle-allowance-radians", "0.3"),
        WFW_ENABLED("drive-wait-for-wheels-enabled", "true", false),
        // Comment to let ; be on new line
        ;

        /**
         * this is just so we can easily change this, rather than commenting out values.
         */
        public final boolean actuallyChangeFromDefault;
        public final String name;
        public final String defaultValue;
        private String value;

        private Key(final String name, final String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.actuallyChangeFromDefault = true;
        }

        private Key(final String name, final String defaultValue, boolean actuallyChangeFromDefault) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            this.actuallyChangeFromDefault = actuallyChangeFromDefault;
        }

        public String get() {
            return value;
        }

        public int getInt() {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Integer.parseInt(defaultValue);
            }
        }

        public double getDouble() {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Double.parseDouble(defaultValue);
            }
        }

        public boolean getBoolean() {
            return ((value != null) && (value.startsWith("y") || value.startsWith("t")));
        }

        public long getLong() {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException ex) {
                System.err.println("Warning: Value '" + value + "' of '" + name + "' is not valid.");
                return Long.parseLong(defaultValue);
            }
        }

        public boolean isInt() {
            try {
                //noinspection ResultOfMethodCallIgnored
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        public boolean isDouble() {
            try {
                //noinspection ResultOfMethodCallIgnored
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        public boolean isBoolean() {
            return value != null && (value.equals("true") || value.equals("false"));
        }
    }

    private final Map<String, Key> keyMap;
    private final RobotTable defaultSettings;
    private final RobotTable driverSettings;
    private final RobotTablesClient client;

    public Settings(final RobotTablesClient client) {
        this.client = client;
        if (client == null) {
            System.err.println("Warning! Failed to initialize settings: RobotTablesClient null.");
            defaultSettings = null;
            driverSettings = null;
            keyMap = null;
            return;
        }
        defaultSettings = client.publishTable("robot-input-default");
        driverSettings = client.subscribeToTable("robot-input");
        keyMap = new HashMap<>(Key.values().length);
        for (Key key : Key.values()) {
            keyMap.put(key.name, key);
        }
    }

    public void subscribeAndPublishDefaults() {
        if (client == null) {
            return;
        }
        client.addClientListener(this, true);
        driverSettings.addUpdateListener(this, true);
        for (Key key : Key.values()) {
            if (key.actuallyChangeFromDefault) {
                defaultSettings.set(key.name, key.defaultValue);
            }
        }
    }

    @Override
    public void onUpdate(final RobotTable table, final String key, final String value, final UpdateAction action) {
        if (!"robot-input".equals(table.getName())) {
            return;
        }
        Key keyObject = keyMap.get(key);
        if (keyObject == null) {
            // They are sending an update to a value we don't know about
            // TODO: Something to prevent this
            return;
        }
        switch (action) {
            case NEW:
            case UPDATE:
                keyObject.value = value;
                break;
            case DELETE:
                keyObject.value = keyObject.defaultValue;
                break;
        }
    }

    @Override
    public void onUpdateAdmin(final RobotTable table, final String key, final String value, final UpdateAction action) {
    }

    @Override
    public void onTableCleared(final RobotTable table) {
        if (!"robot-input".equals(table.getName())) {
            return;
        }
        for (Key key : Key.values()) {
            key.value = key.defaultValue;
        }
    }

    @Override
    public void onTableChangeType(final RobotTable table, final TableType oldType, final TableType newType) {
        if ("robot-input-default".equals(table.getName())) {
            if (newType == TableType.REMOTE) {
                Output.output(OutputLevel.DEBUG, "robot-input-default-local", "No longer local!");
            } else {
                Output.output(OutputLevel.DEBUG, "robot-input-default-local", "Local again now.");
                // Someone else had the table, now we do! Update *all* the values!
                for (Key key : Key.values()) {
                    table.set(key.name, key.defaultValue);
                }
            }
        }
        // We shouldn't ever have this happen for robot-input, because we never attempt to publish it.
    }

    @Override
    public void onTableStaleChange(final RobotTable table, final boolean nowStale) {
        if ("robot-input".equals(table.getName())) {
            Output.output(OutputLevel.DEBUG, "robot-input-stale", nowStale);
            // TODO: Maybe we should have some light on the robot to show this
        }
    }

    @Override
    public void onAllSubscribersStaleChange(final RobotTable table, boolean nowStale) {
        if ("robot-input-default".equals(table.getName())) {
            Output.output(OutputLevel.DEBUG, "robot-input-default-recieved", nowStale);
        }
    }

    @Override
    public void onNewTable(final RobotTable table) {
    }
}
