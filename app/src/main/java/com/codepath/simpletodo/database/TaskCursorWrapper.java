package com.codepath.simpletodo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.codepath.simpletodo.Task;
import com.codepath.simpletodo.database.TaskDatabaseSchema.TaskTable;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String name = getString(getColumnIndex(TaskTable.Cols.NAME));
        long completionDate = getLong(getColumnIndex(TaskTable.Cols.COMPLETION_DATE));
        int isCompleted = getInt(getColumnIndex(TaskTable.Cols.COMPLETED));

        Task task = new Task(UUID.fromString(uuidString));
        task.setName(name);
        task.setDate(new Date(completionDate));
        task.setComplete(isCompleted != 0);
        return task;
    }
}
