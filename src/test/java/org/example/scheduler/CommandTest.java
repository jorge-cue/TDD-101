package org.example.scheduler;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandTest {

    private final Instant i0 = Instant.now();


    @ParameterizedTest(name = "index: delay {0} ms")
    @ValueSource(longs = {0L, 1_000L, 5_000L})
    void delayDecaysAsTimeMovesForward(final long delayInMillis) {
        Command.clock = Clock.fixed(i0, ZoneOffset.UTC);
        var command = new ConcreteCommand(delayInMillis);
        var expectedDelay = delayInMillis / 1_000L;
        var i = i0;
        do {
            var actualDelay = command.getDelay(TimeUnit.SECONDS);

            assertEquals(actualDelay, expectedDelay);

            i = i.plusSeconds(1);
            Command.clock = Clock.fixed(i, ZoneOffset.UTC);
            expectedDelay -= 1;
        } while (expectedDelay >= 0);

    }

    static class ConcreteCommand extends Command {

        public ConcreteCommand(long delayInMillis) {
        }

        public void run() {
        }
    }
}