package com.codepath.simpletodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.codepath.simpletodo.Task;
import com.codepath.simpletodo.database.TaskDatabaseSchema.TaskTable;

public class TaskDao {
    private static TaskDao taskDao;
    private TaskDatabaseHelper taskDatabaseHelper;
    private SQLiteDatabase dataBase;
    private Context context;

    private TaskDao(Context context) {
        this.context = context.getApplicationContext();
        dataBase = TaskDatabaseHelper.getInstance(this.context).getWritableDatabase();
    }

    public static TaskDao getInstance(Context context) {
        if (taskDao == null) {
            taskDao = new TaskDao(context);
        }
        return taskDao;
    }

    public List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        TaskCursorWrapper taskCursorWrapper = queryTasks(null, null);
        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                tasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }
        } finally {
            taskCursorWrapper.close();
        }
        return tasks;
    }

    public void addTask(Task task) {
        dataBase.insert(TaskTable.NAME, null, createTaskContentValues(task));
    }

    public void updateTask(Task task) {
        String uuidString = task.getUuid().toString();
        ContentValues contentValues = createTaskContentValues(task);
        Log.i("TaskDetailsFragment", "TaskDao.updateTask id = " + task.getUuid() + "task date = " + task.getDate());
        dataBase.update(TaskTable.NAME,
                contentValues,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public Task getTaskById(UUID taskId) {
        String whereClause = TaskTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[] {taskId.toString()};
        TaskCursorWrapper taskCursorWrapper = queryTasks(whereClause,whereArgs);
        try {
            if (taskCursorWrapper.getCount() == 0) return null;
            taskCursorWrapper.moveToFirst();
            return taskCursorWrapper.getTask();
        } finally {
            taskCursorWrapper.close();
        }
    }

    private ContentValues createTaskContentValues(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTable.Cols.UUID, task.getUuid().toString());
        contentValues.put(TaskTable.Cols.NAME, task.getName());
        contentValues.put(TaskTable.Cols.COMPLETION_DATE, task.getDate().toString());
        contentValues.put(TaskTable.Cols.COMPLETED, task.isComplete());
        return contentValues;
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = dataBase.query(
                TaskTable.NAME,
                null, // Columns - select all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new TaskCursorWrapper(cursor);
    }

}
