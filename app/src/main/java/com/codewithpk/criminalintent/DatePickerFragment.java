package com.codewithpk.criminalintent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    //create a callbacks interface with a single function called onDateSelected()
    interface Callbacks {
        void onDateSelected(Date date);
    }
    //Adding a newInstance(Date) function
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
        /* return new DatePickerDialog(
                requireContext(),
                null,
                initialYear,
                initialMonth,
                initialDay
        ); */
        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                null,
                initialYear,
                initialMonth,
                initialDay
        );
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date resultDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                if (getTargetFragment() != null) {
                    DatePickerFragment.Callbacks target = (DatePickerFragment.Callbacks) getTargetFragment();
                    target.onDateSelected(resultDate);
                }
            }
        });
        return dialog;
    }
}
