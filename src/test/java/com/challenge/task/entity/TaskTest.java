package com.challenge.task.entity;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void compareTo() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Callable stubCall = () -> null;
        Task task = new Task(now, stubCall);
        int i = task.compareTo(new Task(now.minusSeconds(10), stubCall));
        assertEquals(1, i);

        i = task.compareTo(new Task(now.plusSeconds(10), stubCall));
        assertEquals(-1, i);

        i = task.compareTo(new Task(now, stubCall));
        assertEquals(i, 0);
    }

}