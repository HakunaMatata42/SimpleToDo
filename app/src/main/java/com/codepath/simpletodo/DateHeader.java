package com.codepath.simpletodo;


public class DateHeader implements ListItem {
    private String categoryName;

    public DateHeader(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_DATE_CATEGORY;
    }
}
