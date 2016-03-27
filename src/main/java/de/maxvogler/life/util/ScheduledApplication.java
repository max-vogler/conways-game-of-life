package de.maxvogler.life.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * An Application, where work has to be done at a fixed rate. After calling start(), tick() is called at a fixed rate.
 * To prevent synchronization issues, change the state of the ScheduledApplication only inside change().
 *
 * @param <T>
 */
public abstract class ScheduledApplication<T> {

    private final Object EXECUTOR_LOCK = new Object();

    private int interval;

    private ScheduledExecutorService executor = null;

    public ScheduledApplication(int intervalInMillis) {
        this.interval = intervalInMillis;
    }

    public synchronized void start() {
        synchronized (EXECUTOR_LOCK) {
            if (executor == null) {
                executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(this::synchronizedTick, 0, interval, MILLISECONDS);
            }
        }

    }

    public void stop() {
        synchronized (EXECUTOR_LOCK) {
            if (executor != null) {
                executor.shutdown();
                executor = null;
            }

        }
    }

    private void synchronizedTick() {
        change(__ -> this.tick());
    }

    public abstract void tick();

    public synchronized void change(Consumer<T> fun) {
        fun.accept(getChanges());
    }

    protected abstract T getChanges();

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;

        if (isRunning()) {
            stop();
            start();
        }
    }

    public boolean isRunning() {
        return executor != null;
    }

}
