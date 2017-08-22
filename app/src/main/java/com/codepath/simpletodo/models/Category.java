package com.codepath.simpletodo.models;


import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.simpletodo.R;

import java.util.UUID;

public class Category {
    public static final String COMPLETE = "COMPLETE";
    public static final String INCOMPLETE = "INCOMPLETE";
    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";
    private UUID uuid;
    private String name;

    private Category(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
    }

    static Category[] categories = {
            new Category(LOW),
            new Category(MEDIUM),
            new Category(HIGH)
    };

    public static ArrayAdapter<Category> arrayAdapter(Context context) {
        return new ArrayAdapter<Category>(context,
                android.R.layout.simple_spinner_dropdown_item,
                categories);
    }

    public static int getCategoryPosition(String categoryName) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].getName().equalsIgnoreCase(categoryName)) {
                return i;
            }
        }
        return 0;
    }

    public static ArrayAdapter<Category> taskListArrayAdapter(Context context) {
        Category[] categories = {
                new Category(Category.INCOMPLETE),
                new Category(LOW),
                new Category(MEDIUM),
                new Category(HIGH),
                new Category(Category.COMPLETE)
        };
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(context,
                R.layout.drop_down_textview,
                categories);
        return categoryArrayAdapter;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
