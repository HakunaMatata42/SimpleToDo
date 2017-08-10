package com.codepath.simpletodo;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID uuid;
    private String name;
    private Date date;
    private boolean isComplete;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID uuid) {
        this.uuid = uuid;
        this.date = new Date();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public boolean isComplete() {
        return isComplete;
    }

}
