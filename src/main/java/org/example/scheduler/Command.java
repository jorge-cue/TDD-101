package org.example.scheduler;

import java.time.Clock;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Command implements Delayed, Runnable {

    protected static Clock clock = Clock.systemUTC();

    private final long timeToRun;

    protected Command(long delayInMillis) {
        this.timeToRun = clock.millis() + delayInMillis;
    }

    public final long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(timeToRun - clock.millis(), TimeUnit.MILLISECONDS);
    }

    public final int compareTo(Delayed delayed) {
        return Long.compare(timeToRun, ((Command)delayed).timeToRun);
    }
}
