package com.challenge.task.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TaskWorkerServiceTest extends BaseServiceTest {
    @Autowired
    private TaskWorkerService taskWorkerService;

    @Test
    public void execute() throws Exception {
        taskWorkerService.execute(() -> {
            System.out.println("start 1");
            Thread.sleep(1000);
            System.out.println("end 1");
            return null;
        });
        taskWorkerService.execute(() -> {
            System.out.println("start 2");
            Thread.sleep(1000);
            System.out.println("exception 2");
            throw new Exception();
        });
        taskWorkerService.execute(() -> {
            System.out.println("start 3");
            Thread.sleep(100);
            System.out.println("end 3");
            return null;
        });
        Thread.sleep(5000);

    }

}