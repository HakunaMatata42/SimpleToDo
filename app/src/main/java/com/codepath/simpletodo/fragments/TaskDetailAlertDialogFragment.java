package com.codepath.simpletodo.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class TaskDetailAlertDialogFragment extends DialogFragment {
    public static final String ARG_TITLE = "titleArg";
    public TaskDetailAlertDialogFragment() {
        // Empty Constructor
    }

    public static TaskDetailAlertDialogFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        TaskDetailAlertDialogFragment fragment = new TaskDetailAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Quit without saving?");
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false);
        return alertDialogBuilder.create();
    }
}
