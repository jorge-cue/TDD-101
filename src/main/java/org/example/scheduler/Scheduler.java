package org.example.scheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;

public class Scheduler {
    @SuppressWarnings("unused")
    private final DelayQueue<Command> queue = new DelayQueue<>();

    public void enqueue(Collection<? extends Command> commands) {
        // WIP
    }

    public Collection<CompletableFuture<Void>> launch() {
        return Collections.emptyList();
    }
}
