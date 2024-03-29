package com.codepath.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.Task;
import com.codepath.simpletodo.utils.TaskDatabaseUtil;
import com.codepath.simpletodo.fragments.TaskDetailAlertDialogFragment;
import com.codepath.simpletodo.fragments.TaskDetailsFragment;

import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity implements TaskDetailsFragment.BackPressListener {
    public static final String EXTRA_TASK_ID = "taskId";
    private ViewPager viewPager;
    private boolean isTaskUpdated;

    public static Intent newIntent(Context context, UUID taskId) {
        Intent intent = new Intent(context, TaskPagerActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        viewPager = (ViewPager) findViewById(R.id.task_view_pager);
        isTaskUpdated = false;
        List<Task> tasks = TaskDatabaseUtil.getTasks();
        viewPager.setAdapter(new TaskViewPagerAdapter(getSupportFragmentManager(), tasks));
        setCurrentItem(tasks);
    }

    private void setCurrentItem(List<Task> tasks) {
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getUuid().compareTo(taskId) == 0) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void backButtonPressed(boolean isTaskUpdated) {
        this.isTaskUpdated = isTaskUpdated;
    }

    private class TaskViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Task> tasks;

        public TaskViewPagerAdapter(FragmentManager fragmentManager, List<Task> tasks) {
            super(fragmentManager);
            this.tasks = tasks;
        }

        @Override
        public Fragment getItem(int position) {
            Task task = tasks.get(position);
            return TaskDetailsFragment.newInstance(task.getUuid());
        }

        @Override
        public int getCount() {
            return tasks.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskUpdated) {
            TaskDetailAlertDialogFragment taskDetailAlertDialogFragment = TaskDetailAlertDialogFragment.newInstance("Are you sure?");
            taskDetailAlertDialogFragment.show(getSupportFragmentManager(), "TaskPagerBackPressTitle");
        } else {
            Intent intent = TaskListActivity.newIntent(TaskPagerActivity.this);
            startActivity(intent);
        }
    }
}
