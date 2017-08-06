package com.codepath.simpletodo;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean isComplete;

    public Task() {
        this.id = UUID.randomUUID();
        this.date = new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000);
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

    public UUID getId() {
        return id;
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
