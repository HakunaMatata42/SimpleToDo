package com.codepath.simpletodo.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TaskDatabase.NAME, version = TaskDatabase.VERSION)
public class TaskDatabase {
    public static final String NAME = "Tasks";
    public static final int VERSION = 1;
}
