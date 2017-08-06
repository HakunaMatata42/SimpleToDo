package com.codepath.simpletodo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDao {
    private static TaskDao taskDao;
    private List<Task> tasks;

    private TaskDao() {
        tasks = new ArrayList<>();
    }

    public static TaskDao instance() {
        if (taskDao == null) {
            taskDao = new TaskDao();
        }
        return taskDao;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task getTaskById(UUID taskId) {
        for (Task task : tasks) {
            if (task.getId().compareTo(taskId) == 0) return task;
        }
        return null;
    }
}
