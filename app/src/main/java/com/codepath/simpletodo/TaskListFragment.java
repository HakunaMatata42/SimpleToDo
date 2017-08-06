package com.codepath.simpletodo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String TAG = "TaskListFragment";

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tasklist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TaskAdapter(Task.getTasks()));
        return view;
    }

    private class ListItemTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txvTaskName;
        private TextView txvTaskCompletionDate;
        private Task task;
        public ListItemTaskViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txvTaskName = (TextView) itemView.findViewById(R.id.txvTaskName);
            txvTaskCompletionDate = (TextView) itemView.findViewById(R.id.txvTaskCompletionDate);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), TaskPagerActivity.class);
            intent.putExtra(TaskDetailsFragment.ARG_TASK_ID, task.getId());
            Log.i(TAG, "Setting taskId to " + task.getId());
            startActivity(intent);
        }

        public void bind(Task task) {
            this.task = task;
            txvTaskName.setText(task.getName());
            txvTaskCompletionDate.setText(task.getDate().toString());
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<ListItemTaskViewHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public ListItemTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_task, parent, false);
            return new ListItemTaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ListItemTaskViewHolder holder, int position) {
            holder.bind(tasks.get(position));
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }

}
