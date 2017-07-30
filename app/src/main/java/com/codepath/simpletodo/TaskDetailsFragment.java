package com.codepath.simpletodo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailsFragment extends Fragment {

    private Task task;
    private EditText etTaskName;
    private Button btnTaskCompletionDate;
    private CheckBox chbIsTaskComplete;

    public static final String ARG_TASK_ID = "task_id";
    private static final String TAG = "TaskDetailsFragment";

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
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        Log.i(TAG, "onCreate taskId " + taskId);
        task = Task.get(taskId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        etTaskName.setText(task.getName());
        etTaskName.addTextChangedListener(new TaskNameTextWatcher(task));

        btnTaskCompletionDate = (Button) view.findViewById(R.id.btnTaskCompletionDate);
        btnTaskCompletionDate.setText(task.getDate().toString());
        btnTaskCompletionDate.setEnabled(false);

        chbIsTaskComplete = (CheckBox) view.findViewById(R.id.chbIsTaskComplete);
        chbIsTaskComplete.setChecked(task.isComplete());
        chbIsTaskComplete.setOnCheckedChangeListener(new TaskCompleteOnCheckedChangeListener(task));
        return view;
    }

    private class TaskNameTextWatcher implements TextWatcher {

        private Task task;

        public TaskNameTextWatcher(Task task) {
            this.task = task;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            task.setName(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class TaskCompleteOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        private Task task;

        public TaskCompleteOnCheckedChangeListener(Task task) {
            this.task = task;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            task.setComplete(isChecked);
        }
    }
}
