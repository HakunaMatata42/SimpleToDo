package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.simpletodo.database.TaskDao;

import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "taskId";
    private ViewPager viewPager;
    private TaskDao taskDao;

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
        taskDao = TaskDao.getInstance(this);
        viewPager.setAdapter(new TaskViewPagerAdapter(getSupportFragmentManager(), taskDao.getTasks()));
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        //viewPager.setCurrentItem(Task.taskPosition(taskId));
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

}
