package com.codepath.simpletodo.models;


public interface ListItem {
    int TYPE_DATE_CATEGORY = 0;
    int TYPE_TASK_DATA = 1;

    int getType();
}
