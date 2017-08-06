package com.codepath.simpletodo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        viewPager = (ViewPager) findViewById(R.id.task_view_pager);
        viewPager.setAdapter(new TaskViewPagerAdapter(getSupportFragmentManager(), Task.getTasks()));
        UUID taskId = (UUID) getIntent().getSerializableExtra(TaskDetailsFragment.ARG_TASK_ID);
        Log.i("TaskPagerActivity", "TaskId selected = " + Task.taskPosition(taskId));
        viewPager.setCurrentItem(Task.taskPosition(taskId));
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
            return TaskDetailsFragment.newInstance(task.getId());
        }

        @Override
        public int getCount() {
            return tasks.size();
        }
    }

}
