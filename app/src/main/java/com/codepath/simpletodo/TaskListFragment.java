package com.codepath.simpletodo;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment {

    private static final String TAG = "TaskListFragment";
    private static final String OVERDUE = "OverDue";
    private static final String THISWEEK = "This Week";
    private static final String NEXTWEEK = "Next Week";
    private static final String LATER = "Later";
    private static final DateTime TODAY = new DateTime();
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private Spinner spnCategoryList;
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tasklist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i(TAG, "onCreateView Calling updateUI");
        updateUI(TaskDatabaseUtil.getIncompleteTasks());
        return view;
    }

    private void updateUI(List<Task> tasks) {
        List<ListItem> listItems = consolidatedList(tasks);
        Log.i(TAG, "updateUI taskAdapter listItems size = " + listItems.size());
        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(listItems);
            Log.i(TAG, "setting taskAdapter");
            recyclerView.setAdapter(taskAdapter);
        } else {
            taskAdapter.setListItems(listItems);
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem categoryList = menu.findItem(R.id.category_list);
        spnCategoryList = (Spinner) MenuItemCompat.getActionView(categoryList);
        spnCategoryList.setAdapter(Category.taskListArrayAdapter(getActivity()));
        spnCategoryList.setOnItemSelectedListener(new CategorySelectListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                task.save();
                startTaskPagerActivity(task.getUuid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startTaskPagerActivity(UUID taskId) {
        Intent intent = TaskPagerActivity.newIntent(getActivity(), taskId);
        startActivity(intent);
    }

    private HashMap<String, List<Task>> groupTasksByDateCategory(List<Task> tasks) {
        HashMap<String, List<Task>> tasksByDate = new LinkedHashMap<>();
        List<Task> overdueTasks = new ArrayList<>();
        List<Task> thisWeeksTasks = new ArrayList<>();
        List<Task> nextWeeksTasks = new ArrayList<>();
        List<Task> laterTasks = new ArrayList<>();
        for (Task task : tasks) {
            DateTime taskDate = new DateTime(task.getDate());
            if (isOverDue(task)) {
                overdueTasks.add(task);
            } else if (istThisWeek(task)) {
                thisWeeksTasks.add(task);
            } else if (isNextWeek(task)){
                nextWeeksTasks.add(task);
            } else {
                laterTasks.add(task);
            }
        }
        tasksByDate.put(OVERDUE, overdueTasks);
        tasksByDate.put(THISWEEK, thisWeeksTasks);
        tasksByDate.put(NEXTWEEK, nextWeeksTasks);
        tasksByDate.put(LATER, laterTasks);
        return tasksByDate;
    }

    private boolean isOverDue(Task task) {
        return new DateTime(task.getDate()).getWeekOfWeekyear() < TODAY.getWeekOfWeekyear();
    }

    private boolean istThisWeek(Task task) {
        return new DateTime(task.getDate()).getWeekOfWeekyear() == TODAY.getWeekOfWeekyear();
    }

    private boolean isNextWeek(Task task) {
        return new DateTime(task.getDate()).getWeekOfWeekyear() == (TODAY.getWeekOfWeekyear() + 1);
    }

    private List<ListItem> consolidatedList(List<Task> tasks) {
        List<ListItem> listItems = new ArrayList<>();
        HashMap<String, List<Task>> tasksByDateCategory = groupTasksByDateCategory(tasks);
        for (String dateCategory : tasksByDateCategory.keySet()) {
            List<Task> tasksByDate = tasksByDateCategory.get(dateCategory);
            if (tasksByDate.size() > 0) {
                listItems.add(new DateHeader(dateCategory));
                for (Task task : tasksByDate) {
                    listItems.add(task);
                }
            }
        }
        return listItems;
    }

    private class DateHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView txvDateHeader;
        public DateHeaderViewHolder(View itemView) {
            super(itemView);
            txvDateHeader = (TextView) itemView.findViewById(R.id.txvDateHeader);
        }

        public void bind(DateHeader dateHeader) {
            txvDateHeader.setText(dateHeader.getCategoryName());
        }
    }
    private class ListItemTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txvTaskName;
        private TextView txvTaskCompletionDate;
        private TextView txvTaskCategory;
        private CheckBox chbIsComplete;
        private Task task;
        public ListItemTaskViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txvTaskName = (TextView) itemView.findViewById(R.id.txvTaskName);
            txvTaskCompletionDate = (TextView) itemView.findViewById(R.id.txvTaskCompletionDate);
            txvTaskCategory = (TextView) itemView.findViewById(R.id.txvTaskCategory);
            chbIsComplete = (CheckBox) itemView.findViewById(R.id.chbIsTaskComplete);
            chbIsComplete.setOnCheckedChangeListener(new TaskCompleteListener());
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick taskId = " + task.getUuid());
            startTaskPagerActivity(task.getUuid());
        }

        public void bind(Task task) {
            this.task = task;
            txvTaskName.setText(this.task.getName());
            txvTaskCompletionDate.setText(this.task.formattedDate());
            txvTaskCategory.setText(this.task.getCategory());
            chbIsComplete.setChecked(this.task.isComplete());
            if (isOverDue(task)) {

                int crimson = Color.parseColor("#DC143C");
                txvTaskCompletionDate.setTextColor(crimson);
                txvTaskCategory.setTextColor(crimson);
            }
        }

        private class TaskCompleteListener implements CompoundButton.OnCheckedChangeListener {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i(TAG, "onCheckedChanged...");
                    Toast.makeText(getActivity(), task.getName() + " Completed", Toast.LENGTH_SHORT)
                            .show();
                    //Add this when we have list for complete and incomplete listItems
                    //updateUI(TaskDatabaseUtil.getIncompleteTasks());
                }
                task.setComplete(chbIsComplete.isChecked());
                task.save();
            }
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ListItem> listItems;

        public TaskAdapter(List<ListItem> listItems) {
            this.listItems = listItems;
        }

        public void setListItems(List<ListItem> listItems) {
            this.listItems = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ListItem.TYPE_DATE_CATEGORY: {
                    View itemView = inflater.inflate(R.layout.list_item_dateheader, parent, false);
                    return new DateHeaderViewHolder(itemView);
                }
                case ListItem.TYPE_TASK_DATA: {
                    View itemView = inflater.inflate(R.layout.list_item_task, parent, false);
                    return new ListItemTaskViewHolder(itemView);
                }
                default:
                    throw new IllegalStateException("UnSupported type in onCreateViewHolder");
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            switch (type) {
                case ListItem.TYPE_DATE_CATEGORY: {
                    Log.i(TAG, listItems.get(position).toString());
                    DateHeader dateHeader = (DateHeader) listItems.get(position);
                    DateHeaderViewHolder dateHeaderViewHolder = (DateHeaderViewHolder) holder;
                    dateHeaderViewHolder.bind(dateHeader);
                    break;
                }
                case ListItem.TYPE_TASK_DATA: {
                    Task task = (Task) listItems.get(position);
                    ListItemTaskViewHolder listItemTaskViewHolder = (ListItemTaskViewHolder) holder;
                    listItemTaskViewHolder.bind(task);
                    break;
                }
                default:
                    throw new IllegalStateException("Unsupported type in onBindViewHolder");
            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return listItems.get(position).getType();
        }
    }

    private class CategorySelectListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spnCategoryList.setSelection(position);
            Category category = (Category) spnCategoryList.getSelectedItem();
            updateUI(TaskDatabaseUtil.getTasksByCategory(category));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
