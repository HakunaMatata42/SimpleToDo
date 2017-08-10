package com.codepath.simpletodo.database;


public class TaskDatabaseSchema {
    public static class TaskTable {
        public static final String NAME = "tasks";
        public static class Cols {
            public static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String COMPLETION_DATE = "completionDate";
            public static final String COMPLETED = "completed";
        }
    }
}
