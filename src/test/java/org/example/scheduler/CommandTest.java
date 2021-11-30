package org.example.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("comparesCommandsByTimeToRunArguments")
    void comparesCommandsByTimeToRun(final String description, int createTime1, long delay1, int createTime2, long delay2, int expectedCompare) {
        Command.clock = Clock.fixed(i0.plusSeconds(createTime1), ZoneOffset.UTC);
        var command1 = new ConcreteCommand(delay1);
        Command.clock = Clock.fixed(i0.plusSeconds(createTime2), ZoneOffset.UTC);
        var command2 = new ConcreteCommand(delay2);

        var actualCompare = command1.compareTo(command2);

        assertEquals(expectedCompare, actualCompare);
    }

    static Collection<Arguments> comparesCommandsByTimeToRunArguments() {
        return List.of(
                Arguments.of("To run at same time, created at different times", 1, 0L, 0, 1L, 0),
                Arguments.of("To run at same time, created at same time", 0, 0L, 0, 0L, 0),
                Arguments.of("To run one before second, created at same time", 0, 0L, 0, 1_000L, -1),
                Arguments.of("To run one before second, created at different times", 1, 0L, 2, 0L, -1),
                Arguments.of("To run one after second, created at same time", 0, 2_000L, 0, 0L, -1),
                Arguments.of("To run one after second, created at different time", 0, 5_000L, 1, 0L, -1)
        );
    }

    static class ConcreteCommand extends Command {

        public ConcreteCommand(long delayInMillis) {
            super(delayInMillis);
        }

        public void run() {
        }
    }
}