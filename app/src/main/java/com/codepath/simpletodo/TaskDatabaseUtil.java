package com.codepath.simpletodo;


import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.UUID;

public class TaskDatabaseUtil {
    public static List<Task> getTasks() {
        return SQLite.select()
                .from(Task.class)
                .queryList();
    }

    public static List<Task> getIncompleteTasks() {
        return SQLite.select()
                .from(Task.class)
                .where(Task_Table.name.isNotNull())
                .and(Task_Table.isComplete.eq(false))
                .queryList();
    }

    public static List<Task> getCompleteTasks() {
        return SQLite.select()
                .from(Task.class)
                .where(Task_Table.name.isNotNull())
                .and(Task_Table.isComplete.eq(true))
                .queryList();
    }

    public static List<Task> getTasksByCategory(Category category) {
        Log.i("TaskListFragment", "getTasksByCategory " + category);
        if (category.getName().equals(Category.INCOMPLETE)) {
            return getIncompleteTasks();
        } else if (category.getName().equals(Category.COMPLETE)) {
            return getCompleteTasks();
        } else {
            return SQLite.select()
                    .from(Task.class)
                    .where(Task_Table.name.isNotNull())
                    .and(Task_Table.category.eq(category.getName()))
                    .queryList();
        }
    }

    public static Task getTaskByUuid(UUID uuid) {
        return SQLite.select()
                .from(Task.class)
                .where(Task_Table.uuid.eq(uuid))
                .querySingle();
    }

}
