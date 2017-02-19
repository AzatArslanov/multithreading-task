package com.challenge.task.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class TaskServiceTest extends BaseServiceTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskService taskService;

    @Test
    public void add() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        taskService.add(now.plusSeconds(5), () -> {
            log.info("first");
            return null;
        });
        taskService.add(now.plusSeconds(1), () -> {
            log.info("second");
            return null;
        });
        Thread.sleep(8000);
    }

}