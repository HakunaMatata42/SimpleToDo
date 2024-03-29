package com.codepath.simpletodo.activities;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.codepath.simpletodo.fragments.TaskDetailsFragment;

import java.util.UUID;

public class TaskDetailsActivity extends SingleFragmentActivity {
    private static final String TAG = "TaskDetailsActivity";
    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(TaskDetailsFragment.ARG_TASK_ID);
        Log.i(TAG, "TaskId == " + taskId);
        return TaskDetailsFragment.newInstance(taskId);
    }
}
