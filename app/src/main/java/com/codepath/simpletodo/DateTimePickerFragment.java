package com.codepath.simpletodo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateTimePickerFragment extends DialogFragment {
    private static final String TASK_DATE = "taskDate";
    public static final String EXTRA_SELECTED_DATE = "selectedDate";
    private DatePicker dpDialog;
    public static DateTimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(TASK_DATE, date);

        DateTimePickerFragment dateTimePickerFragment = new DateTimePickerFragment();
        dateTimePickerFragment.setArguments(args);
        return dateTimePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_date, null);
        Date taskDate = (Date) getArguments().getSerializable(TASK_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(taskDate);

        dpDialog = (DatePicker) view.findViewById(R.id.dpDialog);
        dpDialog.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.task_date_label)
                .setPositiveButton(android.R.string.ok, new DatePickerOnclick())
                .create();
    }

    private void sendSelectedDateResult(int resultCode, Date date) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private class DatePickerOnclick implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Date date =  new GregorianCalendar(dpDialog.getYear(),
                    dpDialog.getMonth(),
                    dpDialog.getDayOfMonth())
                    .getTime();
            sendSelectedDateResult(Activity.RESULT_OK, date);
        }
    }
}
