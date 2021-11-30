package org.example.scheduler;

import java.time.Clock;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public abstract class Command implements Delayed, Runnable {

    protected static Clock clock = Clock.systemUTC();

    private final long timeToRun;

    protected Command(long delayInMillis) {
        checkState(clock != null, "Command.clock is not set");
        this.timeToRun = clock.millis() + delayInMillis;
    }

    public final long getDelay(TimeUnit timeUnit) {
        checkNotNull(timeUnit, "argument 'timeUnit' is required");
        checkState(clock != null, "Command.clock is not set");
        return timeUnit.convert(timeToRun - clock.millis(), TimeUnit.MILLISECONDS);
    }

    public final int compareTo(Delayed delayed) {
        checkNotNull(delayed, "argument 'delayed' is required");
        checkArgument(delayed instanceof Command, "argument 'delayed' is not a Command");
        return Long.compare(timeToRun, ((Command)delayed).timeToRun);
    }
}
