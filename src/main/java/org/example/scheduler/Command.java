package org.example.scheduler;

import java.time.Clock;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Command implements Delayed, Runnable {

    protected static Clock clock = Clock.systemUTC();

    public final long getDelay(TimeUnit timeUnit) {
        return 0;
    }

    public final int compareTo(Delayed delayed) {
        return 0;
    }
}
