package com.challenge.task.service;

import com.challenge.task.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile Thread manager = null;
    private volatile boolean isStopping = false;

    private final BlockingQueue<Task> queue = new DelayQueue<>();
    private final TaskWorkerService taskWorkerService;

    @Autowired
    public TaskService(TaskWorkerService taskWorkerService) {
        this.taskWorkerService = taskWorkerService;
    }

    public void add(LocalDateTime localDateTime, Callable callable) {
        Task task = new Task(localDateTime, callable);
        log.info("add new task in queue: {}", task);
        try {
            queue.put(task);
        } catch (InterruptedException ignored) {
            log.error("Queue is full", ignored);
            throw new RuntimeException(ignored);
        }
    }

    @PostConstruct
    public synchronized void init() {
        isStopping = false;
        if (manager == null) {
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
        }
    }

    public void stop() {
        isStopping = true;
    }
}
