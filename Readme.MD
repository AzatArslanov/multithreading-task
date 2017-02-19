# Пример использования DelayQueue
Есть проблема:

> На вход поступают пары (LocalDateTime, Callable). Необходимо реализовать систему, которая будет выполнять Callable для каждого пришедшего события в указанный LocalDateTime. Задачи должны выполняться в порядке согласно значению LocalDateTime. 
Если на один момент времени указано более одной задачи, то порядок их выполнения определяется порядком поступления. Если система перегружена, то задачи, время выполнения которых оказалось в прошлом, все равно должны выполниться согласно приоритетам описанным выше. 
Задачи могут приходить в произвольном порядке и из разных потоков.

Для ее решения отлично подойдет DelayQueue из пакета java.util.concurrent. DelayQueue это "неограниченная" (максимальное количество элементов Integer.MAX_VALUE) блокирующая очередь которая содержит элементы реализующие интерфейс Delayed. Особенностью этой очереди является то, что элементы будут доступны только после того, как их метод getDelay будет возвращать значение меньше или раное 0. Внутри себя содержит PriorityQueue поэтому также очень важно правильная реализация метода compareTo для наших элементов, так, чтобы во главе очереди оказывались элементы которые в первую очередь стоит выполнить.
Для нашей проблемы реализация класса с задачей будет такой:
```java
public class Task implements Delayed {

    private final Callable callable;
    private final LocalDateTime dateTime;

    public Task(LocalDateTime dateTime, Callable callable) {
        if (dateTime == null || callable == null) {
            throw new NullPointerException("dateTime and callable must be set");
        }
        this.dateTime = dateTime;
        this.callable = callable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Callable getCallable() {
        return callable;
    }

    @Override
    public int compareTo(Delayed o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof Task) {
            LocalDateTime otherDateTime = ((Task) o).getDateTime();
            return dateTime.compareTo(otherDateTime);
        }
        return 1;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(Duration.between(LocalDateTime.now(), dateTime).getSeconds(), TimeUnit.SECONDS);
    }
}
```

Непосредственно выполнением Callable будет заниматься Executors.newSingleThreadExecutor(), а загружать задачами его будет отдельный поток
```java
manager = new Thread(() -> {
                while (!isStopping) {
                    try {
                        log.info("wait new tasks");
                        Task task = queue.take();
                        log.info("received {}", task);
                        taskWorkerService.execute(task.getCallable());
                    } catch (InterruptedException ignored) {
                        log.error("Queue throws interrupted exception", ignored);
                        throw new RuntimeException(ignored);
                    }
                }

            }, "TaskService.manager");
            manager.start();
```
