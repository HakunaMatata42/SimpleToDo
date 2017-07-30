package com.codepath.simpletodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Task {
    private static ArrayList<Task> tasks;
    private UUID id;
    private String name;
    private Date date;
    private boolean isComplete;

    public Task() {
        this.id = UUID.randomUUID();
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

    public static List<Task> getTasks() {
        tasks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Task task =  new Task();
            task.setName("Task #"+i);
            task.setComplete(random.nextInt(100)%2 == 0);
            tasks.add(task);
        }
        return tasks;
    }

    public static Task get(UUID id) {
        for (Task task : tasks) {
            if (task.id.compareTo(id) == 0) return task;
        }
        return null;
    }
}
