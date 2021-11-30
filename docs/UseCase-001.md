# UC-001: Run an Action after a waiting time

As a developer I need a mechanism to control actions execution to be run in a specific time in the future, independently 
of conditions of creation. Each Action carries all required information for itś execution.

## Diagrams
### Sequence
```mermaid
sequenceDiagram
    participant Origin
    participant Command
    participant Scheduler
    Origin ->> Command: new
    Origin -) Scheduler: enqueue
    System -) Scheduler: execute @second
    Scheduler ->> Executor: add Ready commands
    Executor -) Command: run
```
### Class diagram
```mermaid
classDiagram
    class Delayed {
        <<interface>>
        +getDelay(unit: TimeUnit) long
        +compareTo(other:Delayable) int
    }
    class Runnable {
        <<interface>>
        +run(): void
    }
    class Command {
        <<abstract>>
        #Clock clock$
        #long timeToRun
        +getDelay(unit: TimeUnit) long
        +compareTo(other:Delayable) int
        +run() void*
    }
    Delayed <|.. Command
    Runnable <|.. Command
    class Scheduler {
        <<component>>
        -DelayQueue~Command~ queue
        +enqueue(commands: Collection<? extends Command>) void
        +execute() void
        #launch() Collection~CompletableFuture~
    }
```
## Implementation

We will implement Command class, then Scheduler; executing next steps:

- Write Unit tests for Command class; and make sure it fails with expected messages.
- 