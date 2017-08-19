package com.codepath.simpletodo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailsFragment extends Fragment {

    private EditText etTaskName;
    private Button btnTaskCompletionDate;
    private CheckBox chbIsTaskComplete;
    private Spinner spnTaskCategory;

    private Task task;
    private boolean isTaskNameUpdated;
    private boolean isTaskDateUpdated;
    private boolean isTaskCompletionStatusUpdated;
    private boolean isTaskCategoryUpdated;

    public static final String ARG_TASK_ID = "task_id";
    private static final String TAG = "TaskDetailsFragment";
    private static final String DATE_DIALOG_FRAGMENT_TAG = "DateDialog";
    private static final String ALERT_DIALOG_FRAGMENT_TAG = "AlertDialog";
    private static final int REQUEST_CODE_DATE = 0;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(UUID taskId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, taskId);

        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        taskDetailsFragment.setArguments(bundle);
        return taskDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isTaskNameUpdated = false;
        isTaskDateUpdated = false;
        isTaskCompletionStatusUpdated = false;
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        task = TaskDatabaseUtil.getTaskByUuid(taskId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task_details, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        isTaskNameUpdated = false;
        isTaskDateUpdated = false;
        isTaskCompletionStatusUpdated = false;

        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        etTaskName.setText(task.getName());
        etTaskName.addTextChangedListener(new TaskNameTextWatcher());

        btnTaskCompletionDate = (Button) view.findViewById(R.id.btnTaskCompletionDate);
        updateDateUi();
        btnTaskCompletionDate.setOnClickListener(new DateButtonClickListener());

        chbIsTaskComplete = (CheckBox) view.findViewById(R.id.chbIsTaskComplete);
        chbIsTaskComplete.setChecked(task.isComplete());
        chbIsTaskComplete.setOnCheckedChangeListener(new TaskCompleteOnCheckedChangeListener());

        spnTaskCategory = (Spinner) view.findViewById(R.id.spnTaskCategory);
        ArrayAdapter<Category> adapter = Category.arrayAdapter(getActivity());
        spnTaskCategory.setAdapter(adapter);
        spnTaskCategory.setSelection(0, false); //This is to stop the firing of OnItemSelectedListener when setting up the spinner
        updateSpinnerUi();
        spnTaskCategory.setOnItemSelectedListener(new CategoryOnItemSelectedListener());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlertDialog("Are you sure?");
                return true;

            case R.id.save_task:
                task.save();
                startActivity(TaskListActivity.newIntent(getActivity()));
                return true;

            case R.id.delete_task:
                task.delete();
                startActivity(TaskListActivity.newIntent(getActivity()));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult requestCode = " + requestCode + " resultCode = " + resultCode);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CODE_DATE) {
            Date date = (Date) data.getSerializableExtra(DateTimePickerFragment.EXTRA_SELECTED_DATE);
            task.setDate(date);
            updateDateUi();
        }
    }

    private void updateDateUi() {
        btnTaskCompletionDate.setText(task.formattedDate());
    }

    private void showAlertDialog(String title) {
        if (isTaskNameUpdated || isTaskDateUpdated || isTaskCompletionStatusUpdated || isTaskCategoryUpdated) {
            TaskDetailAlertDialogFragment taskDetailAlertDialogFragment = TaskDetailAlertDialogFragment.newInstance(title);
            taskDetailAlertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_FRAGMENT_TAG);
        } else {
            startActivity(TaskListActivity.newIntent(getActivity()));
        }
    }

    private class TaskNameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            isTaskNameUpdated =  true;
            task.setName(s.toString());
        }
    }

    private class TaskCompleteOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isTaskCompletionStatusUpdated = true;
            task.setComplete(isChecked);
        }
    }

    private class DateButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            isTaskDateUpdated = true;
            DateTimePickerFragment dateTimePickerFragment = DateTimePickerFragment.newInstance(task.getDate());
            dateTimePickerFragment.setTargetFragment(TaskDetailsFragment.this, REQUEST_CODE_DATE);
            dateTimePickerFragment.show(getFragmentManager(), DATE_DIALOG_FRAGMENT_TAG);
        }
    }

    private class CategoryOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spnTaskCategory.setSelection(position);
            updateSpinnerUi();
            isTaskCategoryUpdated = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void updateSpinnerUi() {
        Category selectedCategory = (Category) spnTaskCategory.getSelectedItem();
        task.setCategory(selectedCategory.getName());
    }

}
