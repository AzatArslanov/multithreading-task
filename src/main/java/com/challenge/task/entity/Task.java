package com.challenge.task.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

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

    @Override
    public String toString() {
        return "Task{" +
                "callable=" + callable +
                ", dateTime=" + dateTime +
                '}';
    }
}
