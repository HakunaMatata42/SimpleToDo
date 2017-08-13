package com.codepath.simpletodo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.database.TaskDatabaseSchema.TaskTable;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "TaskDatabaseHelper";
    private static final String DB_NAME = "tasksDatabase";
    private static final int DB_VERSION = 4;

    private static TaskDatabaseHelper taskDatabaseHelper;

    public static TaskDatabaseHelper getInstance(Context context) {
        Log.i(TAG, "getInstance");
        if (taskDatabaseHelper == null) {
            taskDatabaseHelper = new TaskDatabaseHelper(context);
        }
        return taskDatabaseHelper;
    }

    private TaskDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i(TAG, "TaskDatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreateDatabase");
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TaskTable.NAME +
                "(" +
                    TaskTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TaskTable.Cols.UUID + " BLOB," +
                    TaskTable.Cols.NAME + " TEXT," +
                    TaskTable.Cols.COMPLETION_DATE + " TEXT," +
                    TaskTable.Cols.COMPLETED + " INTEGER," +
                    TaskTable.Cols.CATEGORY + "TEXT" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TaskTable.NAME);
            onCreate(db);
        }
    }
}
