package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "taskId";
    private ViewPager viewPager;

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
        Log.i("TaskPagerActivity", "onBackPressed()");
        TaskDetailAlertDialogFragment taskDetailAlertDialogFragment = TaskDetailAlertDialogFragment.newInstance("Are you sure?");
        taskDetailAlertDialogFragment.show(getSupportFragmentManager(), "BackTitle");
    }
}
