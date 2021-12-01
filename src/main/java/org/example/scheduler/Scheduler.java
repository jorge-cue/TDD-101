package org.example.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

public class Scheduler {

    private final DelayQueue<Command> queue = new DelayQueue<>();

    public void enqueue(Collection<? extends Command> commands) {
        queue.addAll(commands);
    }

    public Collection<CompletableFuture<Void>> launch() {
        Collection<? super Command> ready = new ArrayList<>();
        queue.drainTo(ready);
        return ready.stream().map(Runnable.class::cast).map(CompletableFuture::runAsync).collect(Collectors.toList());
    }
}
