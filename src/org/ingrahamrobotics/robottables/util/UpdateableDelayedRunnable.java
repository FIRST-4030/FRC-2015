package org.ingrahamrobotics.robottables.util;

// TODO: We need some way to insert random tasks into the main handling thread from here, so that we don't face concurrency issues.
public class UpdateableDelayedRunnable implements Runnable {

    private final Runnable runWhenDone;
    private final Object updateLock = new Object();
    private boolean updaterRunning = false;
    private long timeoutTime;

    public UpdateableDelayedRunnable(final Runnable done) {
        runWhenDone = done;
    }

    public void delayUntil(long timeoutTime) {
        synchronized (updateLock) {
            this.timeoutTime = timeoutTime;
            if (!updaterRunning) {
                new Thread(this).start();
            }
        }
    }

    public void run() {
        long currentUpdateTime;
        synchronized (updateLock) {
            if (updaterRunning) {
                return;
            }
            updaterRunning = true;
            currentUpdateTime = timeoutTime;
        }
        while (true) {
            long waitTime = currentUpdateTime - System.currentTimeMillis();
            if (waitTime > 0) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (updateLock) {
                if (timeoutTime > currentUpdateTime) {
                    // If the updateTime has changed since we started, we should sleep again.
                    currentUpdateTime = timeoutTime;
                } else {
                    // Otherwise, let's run it!
                    updaterRunning = false;
                    break;
                }
            }
        }
        runWhenDone.run();
    }
}
