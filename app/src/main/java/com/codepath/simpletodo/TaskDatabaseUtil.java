package com.codepath.simpletodo;


import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;
import java.util.UUID;

public class TaskDatabaseUtil {
    public static List<Task> getTasks() {
        return SQLite.select()
                .from(Task.class)
                .queryList();
    }

    public static Task getTaskByUuid(UUID uuid) {
        return SQLite.select()
                .from(Task.class)
                .where(Task_Table.uuid.eq(uuid))
                .querySingle();
    }
}
