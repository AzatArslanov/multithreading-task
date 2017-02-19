package com.challenge.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TaskWorkerService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public Future execute(Callable callable) {
        log.info("execute task");
        return executorService.submit(callable);
    }

    @PreDestroy
    public void preDestroy() {
        log.info("Shutdown executor service");
        executorService.shutdownNow();
    }

}
