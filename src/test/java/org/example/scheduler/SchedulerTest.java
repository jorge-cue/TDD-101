package org.example.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SchedulerTest {

    // Subject Under Test, Scheduler
    private Scheduler scheduler;

    // Access to Internal Scheduler's Queue
    private DelayQueue<Command> queue;

    // Start of test universe
    private static final Instant I0 = Instant.now();

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Command.clock = Clock.fixed(I0, ZoneOffset.UTC);

        scheduler = new Scheduler();

        // To get access to scheduler's queue we use Reflection
        Field field = scheduler.getClass().getDeclaredField("queue");
        field.setAccessible(true);
        //noinspection unchecked
        queue = (DelayQueue<Command>) field.get(scheduler);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("enqueueingAffectsQueueAsExpectedCases")
    void enqueueingAffectsQueueAsExpected(String name, Collection<Command> commands, int expectedFinalSIze) {
        var originalSize = queue.size();

        scheduler.enqueue(commands);

        var finalSize = queue.size();
        assertAll(
                () -> assertEquals(0, originalSize, "queue must be empty at the beginning"),
                () -> assertEquals(expectedFinalSIze, finalSize, "queue must contain the commands just enqueued")
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("commandsRunWhenDelayIsOverCases")
    void commandsRunWhenDelayIsOver(String name, List<Long> delays, List<Integer> runs) {
        var initialQueueSize = queue.size();
        List<Command> commands = delays.stream().map(ConcreteCommand::new).collect(Collectors.toList());
        scheduler.enqueue(commands);

        assertEquals(0, initialQueueSize);
        assertEquals(commands.size(), queue.size());

        Instant i = I0; Command.clock = Clock.fixed(i, ZoneOffset.UTC);
        for(Integer runSize: runs) {
            Collection<CompletableFuture<Void>> running = scheduler.launch();
            assertEquals(runSize, running.size());
            i = i.plusSeconds(1); Command.clock = Clock.fixed(i, ZoneOffset.UTC);
        }
        assertEquals(0, queue.size());
    }

    static Stream<Arguments> enqueueingAffectsQueueAsExpectedCases() {
        return Stream.of(
                Arguments.of("enqueueing an empty list leaves the queue as originally found", Collections.emptyList(), 0),
                Arguments.of("enqueueing a Single command list increases the queue by 1", Collections.singletonList(new ConcreteCommand(0)), 1),
                Arguments.of("enqueueing a 3 commands list increases the queue by 3",
                        List.of(new ConcreteCommand(0), new ConcreteCommand(1), new ConcreteCommand(2)),
                        3)
        );
    }

    static Stream<Arguments> commandsRunWhenDelayIsOverCases() {
        return Stream.of(
                Arguments.of("A single command running immediately", List.of(0L), List.of(1)),
                Arguments.of("A single command running immediately, a second command 5 seconds later", List.of(0L, 5_000L), List.of(1, 0, 0, 0, 0, 1))
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