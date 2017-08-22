package com.codepath.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.codepath.simpletodo.fragments.TaskListFragment;

public class TaskListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, TaskListActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
