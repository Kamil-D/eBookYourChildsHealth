package com.example.kamil.ebookyourchildshealth.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.example.kamil.ebookyourchildshealth.R;

import java.util.Calendar;

import butterknife.OnClick;

/**
 * Created by Kamil on 2016-12-06.
 */

public class MyDatePicker {

    private Calendar calendar;
    private int day, month, year;
    private String dateString = "";

    public MyDatePicker(Activity activity) {
        setCurrentDate();

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, datePickerListener,
                year, month, day);

        datePickerDialog.show();
    }

    private void setCurrentDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) // +1 bo miesiące numeruje od 0
                            + "/" +String.valueOf(year);
                    setDateString(date);
                }
            };

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

}
