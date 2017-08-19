package com.codepath.simpletodo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Table(database = TaskDatabase.class)
public class Task extends BaseModel implements ListItem {
    @Column
    @PrimaryKey
    private UUID uuid;

    @Column
    private String name;

    @Column
    private Date date;

    @Column
    private boolean isComplete;

    @Column
    private String category;

    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID uuid) {
        this.uuid = uuid;
        this.date = new Date();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String formattedDate() {
        return new SimpleDateFormat("E, MMM d, yyyy").format(date);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int getType() {
        return ListItem.TYPE_TASK_DATA;
    }
}
