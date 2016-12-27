package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Visit;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private long eventID;
    private MyDatabaseHelper myDatabaseHelper;
    private String[] textViewLeftColumnNamesArray;
    private int idMedicalVisit;
    private int childIDFromIntent;
    private Visit visitObject;
    private Visit visitUpdatedObject;
    private int day, month, year, hour, minute;
    private Calendar calendar;
    private static Context context;

    @BindString(R.string.pick_date)
    String pickDateString;

    @BindView(R.id.columnVisitName)
    TextView textViewName;

    @BindView(R.id.columnDoctor)
    TextView textViewDoctor;

    @BindView(R.id.columnDisease)
    TextView textViewDisease;

    @BindView(R.id.columnDate)
    TextView textViewDate;

    @BindView(R.id.columnDescription)
    TextView textViewDescription;

    @BindView(R.id.columnRecommendations)
    TextView textViewRecommendations;

    @BindView(R.id.columnMedicines)
    TextView textViewMedicines;

    @BindView(R.id.columnVisitNameValue)
    TextView textViewNameValue;

    @BindView(R.id.columnDoctorValue)
    TextView textViewDoctorValue;

    @BindView(R.id.columnDiseaseValue)
    TextView textViewDiseaseValue;

    @BindView(R.id.columnDateValue)
    TextView textViewDateValue;

    @BindView(R.id.columnDescriptionValue)
    TextView textViewDescriptionValue;

    @BindView(R.id.columnRecommendationsValue)
    TextView textViewRecommendationsValue;

    @BindView(R.id.columnMedicinesValue)
    TextView textViewMedicinesValue;

    @BindView(R.id.buttonVisitReminder)
    Button buttonAddVisitReminder;

    @BindView(R.id.buttonUpdateVisit)
    Button buttonUpdateVisit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_medical_visit, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());
        context = getActivity();
        buttonUpdateVisit.setVisibility(View.GONE);

        eventID = 666;

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
        getMedicalVisitDataFromDatabase();
        setTextOnLeftColumnTextView();
        setDataFromDBOnLeftColumnTextView();
        createListeners();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 70);
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }

    private void getBundleFromIntent() {
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        idMedicalVisit = bundle.getInt("idObjectToShow");
        childIDFromIntent = bundle.getInt("childIDFromIntent");

    }

    public void getMedicalVisitDataFromDatabase() {
        Cursor cursor = myDatabaseHelper.readMedicalVisitData(idMedicalVisit);
        visitObject = new Visit();

        if(cursor.getCount() == 0) {
            return;
        }
        while(cursor.moveToNext()) {
            visitObject.setName(cursor.getString(2));
            visitObject.setDoctor(cursor.getString(3));
            visitObject.setDiseaseId(cursor.getInt(4));
            visitObject.setDate(cursor.getString(5));
            visitObject.setDescription(cursor.getString(6));
            visitObject.setRecommendations(cursor.getString(7));
            visitObject.setMedicines(cursor.getString(8));

            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_object_info);
            toolbar.setTitle("Wizyta z dnia " + visitObject.getDate());
        }
    }

    private void setTextOnLeftColumnTextView() {
        Resources resources = getContext().getResources();
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.visit_table);

        textViewName.setText(textViewLeftColumnNamesArray[0]);
        textViewDoctor.setText(textViewLeftColumnNamesArray[1]);
        textViewDisease.setText(textViewLeftColumnNamesArray[2]);
        textViewDate.setText(textViewLeftColumnNamesArray[3]);
        textViewDescription.setText(textViewLeftColumnNamesArray[4]);
        textViewRecommendations.setText(textViewLeftColumnNamesArray[5]);
        textViewMedicines.setText(textViewLeftColumnNamesArray[6]);
    }

    private void setDataFromDBOnLeftColumnTextView() {
        textViewNameValue.setText(visitObject.getName());
        textViewDoctorValue.setText(visitObject.getDoctor());
        textViewDiseaseValue.setText(getDiseaseDataFromDatabase());
        textViewDateValue.setText(visitObject.getDate());
        textViewDescriptionValue.setText(visitObject.getDescription());
        textViewRecommendationsValue.setText(visitObject.getRecommendations());
        textViewMedicinesValue.setText(visitObject.getMedicines());
    }

    private String getDiseaseDataFromDatabase() {
        Cursor cursor = myDatabaseHelper.readDiseaseData(visitObject.getDiseaseId());
        String nameDateDisease = "";

        if(cursor.getCount() == 0) {
            nameDateDisease = "Nie przypisano choroby";
            return nameDateDisease;
        }
        while(cursor.moveToNext()) {
            nameDateDisease = cursor.getString(2) + " - " + cursor.getString(3);
        }
        return nameDateDisease;
    }

    private void createListeners() {

        textViewNameValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogToChangeValue(textViewNameValue.getText().toString(), v);
                return true;
            }
        });

        textViewDoctorValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogToChangeValue(textViewDoctorValue.getText().toString(), v);
                return true;
            }
        });

        textViewDescriptionValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogToChangeValue(textViewDescriptionValue.getText().toString(), v);
                return true;
            }
        });

        textViewMedicinesValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogToChangeValue(textViewMedicinesValue.getText().toString(), v);
                return true;
            }
        });

        textViewRecommendationsValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogToChangeValue(textViewRecommendationsValue.getText().toString(), v);
                return true;
            }
        });

    }

    public void showDialogToChangeValue(String str, View view) {
        final TextView textView = (TextView) view;

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Edytuj dane:");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_edit_view, null);
        final EditText editTextDialog = (EditText) myView.findViewById(R.id.text_view_dialog);
        editTextDialog.setText(str);
        builder.setView(myView);
        builder.setNegativeButton("Anuluj",null);
        builder.setPositiveButton("Potwiedź", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(editTextDialog.getText().toString());
                showOrHideButtonEditVisit();
            }
        });

        builder.show();
    }

    public static Context getAppContext(){
        return context;
    }

    private void showOrHideButtonEditVisit() {
        if (checkIfDataEdited())
            buttonUpdateVisit.setVisibility(View.VISIBLE);
        else if (!checkIfDataEdited())
            buttonUpdateVisit.setVisibility(View.GONE);
    }

    private boolean checkIfDataEdited() {
        getEditedDataFromTextViews();

        boolean ifEdited = false;

        if (!visitUpdatedObject.getName().equals(visitObject.getName()))
            ifEdited = true;
        else if (!visitUpdatedObject.getDoctor().equals(visitObject.getDoctor()))
            ifEdited = true;
        else if (!visitUpdatedObject.getDate().equals(visitObject.getDate()))
            ifEdited = true;
        else if (!visitUpdatedObject.getDescription().equals(visitObject.getDescription()))
            ifEdited = true;
        else if (!visitUpdatedObject.getRecommendations().equals(visitObject.getRecommendations()))
            ifEdited = true;
        else if (!visitUpdatedObject.getMedicines().equals(visitObject.getMedicines()))
            ifEdited = true;

        return ifEdited;
    }

    private void getEditedDataFromTextViews() {
        visitUpdatedObject = new Visit();

        visitUpdatedObject.setName(textViewNameValue.getText().toString());
        visitUpdatedObject.setDoctor(textViewDoctorValue.getText().toString());
        visitUpdatedObject.setDate(textViewDateValue.getText().toString());
        visitUpdatedObject.setDescription(textViewDescriptionValue.getText().toString());
        visitUpdatedObject.setRecommendations(textViewRecommendationsValue.getText().toString());
        visitUpdatedObject.setMedicines(textViewMedicinesValue.getText().toString());
    }

    @OnClick(R.id.buttonUpdateVisit)
    public void updateVisitToDatabase(View v) {
        visitObject = new Visit();

        if (checkIfAllFieldAreFilled()) {
//            visitObject.setChildId(childIDFromIntent);
            visitObject.setName(textViewNameValue.getText().toString());
            visitObject.setDoctor(textViewDoctorValue.getText().toString());
            visitObject.setDate(textViewDateValue.getText().toString());
            visitObject.setDescription(textViewDescriptionValue.getText().toString());
            visitObject.setRecommendations(textViewRecommendationsValue.getText().toString());
            visitObject.setMedicines(textViewMedicinesValue.getText().toString());

            boolean isInserted = myDatabaseHelper.updateMedicalVisitData(visitObject, idMedicalVisit);

            if (isInserted == true)
                Toast.makeText(getActivity(), "Dane zapisane", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Dane nie zostały zapisane", Toast.LENGTH_LONG).show();

            getActivity().setResult(UtilCode.RESULT_CODE, null);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "UZUPEŁNIJ WSZYSTKIE POLA!", Toast.LENGTH_LONG).show();
    }

    private boolean checkIfAllFieldAreFilled() {
        if ( textViewNameValue.getText().toString().matches("") ||
                textViewDoctorValue.getText().toString().matches("") ||
                textViewDiseaseValue.getText().toString().matches("") ||
                textViewDateValue.getText().toString().matches(pickDateString) ||
                textViewDescriptionValue.getText().toString().matches("") ||
                textViewRecommendationsValue.getText().toString().matches("") ||
                textViewMedicinesValue.getText().toString().matches(""))
            return false;
        return true;
    }


    @OnClick(R.id.buttonDeleteVisitReminder)
    public void deleteReminder(View v) {
//        ContentResolver cr = getActivity().getContentResolver();
//        ContentValues values = new ContentValues();
//        Uri deleteUri = null;
//        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
//        int rows = getActivity().getContentResolver().delete(deleteUri, null, null);

        Toast.makeText(getActivity(), "KASOWANIE WIZYTY", Toast.LENGTH_LONG).show();

        int iNumRowsDeleted = 0;

        Uri baseUri = Uri.parse("content://com.android.calendar/events");

        Uri deleteUri;

        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(eventID)));

        int rows = getActivity().getContentResolver().delete(deleteUri, null, null);

        if (rows>0)
            Toast.makeText(getActivity(), "WIZYTA SKASOWANA!", Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.buttonVisitReminder)
    public void showDateAndTimePickerDialog(View v) {
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        setCurrentDateAndTime();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,datePickerListener,
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timePickerListener,
                hour, minute, true);
        timePickerDialog.show();
    }

    private void setCurrentDateAndTime() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    setDateOnVariables(year, monthOfYear, dayOfMonth);
                    showTimePickerDialog();
                }
            };

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                    setTimeOnVariables(selectedHour, selectedMinute);
                    setReminder();
                }
            };

    private void setDateOnVariables(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    private void setTimeOnVariables(int selectedHour, int selectedMinute) {
        this.hour = selectedHour;
        this.minute = selectedMinute;
    }

    private void setReminder() {
        ContentValues values = new ContentValues();

        String dateString = String.valueOf(day) + "." + String.valueOf(month) + "."
                + String.valueOf(year) + " " + String.valueOf(hour) + ":" + String.valueOf(minute);

        long startTime;
        long endTime;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        startTime = calendar.getTimeInMillis();
        endTime = startTime;

        // Add to Android db; duration is null for nonrecurring events.
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.DTSTART, startTime );
        values.put(CalendarContract.Events.DTEND, endTime );
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.TITLE, visitObject.getName() + " - " + getDiseaseDataFromDatabase());
        values.put(CalendarContract.Events.DESCRIPTION, visitObject.getName() + " - " + getDiseaseDataFromDatabase());

        Uri baseUri = Uri.parse("content://com.android.calendar/events");
//        if (Build.VERSION.SDK_INT >= 8) {
//            baseUri = Uri.parse("content://com.android.calendar/events");
//        } else {
//            baseUri = Uri.parse("content://calendar/events");
//        }

        Uri event = getContext().getContentResolver().insert(baseUri, values);

        eventID = Long.parseLong(event.getLastPathSegment());

        values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, 0);

        Uri baseUri2 = Uri.parse("content://com.android.calendar/reminders");
//        if (Build.VERSION.SDK_INT >= 8) {
//            baseUri2 = Uri.parse("content://com.android.calendar/reminders");
//        } else {
//            baseUri2 = Uri.parse("content://calendar/reminders");
//        }

        Uri uri = getContext().getContentResolver().insert(baseUri2, values);

        showToastInfo(dateString);
    }

    private void showToastInfo(String dateString) {
        Toast toast = Toast.makeText(getActivity(), "PRZYPOMNIENIE ZOSTAŁO USTAWIONE NA " + dateString, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(30);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER, 0, 0);
        toast.show();
    }

}