package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Reminder;
import com.example.kamil.ebookyourchildshealth.model.Visit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by KamilosD on 2016-12-29.
 */

public class InfoMedicalVisitTabTwoFragment extends Fragment {

    static MyDebugger myDebugger;
    private long eventID;
    private MyDatabaseHelper myDatabaseHelper;
    private int idMedicalVisit;
    private Visit visitObject;
    private Reminder reminderObject;
    private static ArrayList<Reminder> reminderRecyclerViewItemArrayList;
    private int day, month, year, hour, minute;
    private Calendar calendar;
    private String dateString;
    private int idReminder;
    private static Context context;

    @BindView(R.id.recycler_view_reminders)
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_medical_visit_tab_two, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());
        context = getActivity();

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
        getMedicalVisitDataFromDatabase();
        getReminderDataFromDatabase();
        createAndSetContentAdapter();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }

    public static Context getAppContext(){
        return context;
    }

    private void getBundleFromIntent() {
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        idMedicalVisit = bundle.getInt("idObjectToShow");
    }

    private void customRefreshRecyclerView() {
        getReminderDataFromDatabase();
        createAndSetContentAdapter();
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

        }
    }

    private void getReminderDataFromDatabase() {

        reminderRecyclerViewItemArrayList= new ArrayList<>();
        Reminder reminder;

        Cursor cursor = myDatabaseHelper.readSingleVisitRemindersData(idMedicalVisit);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            reminder = new Reminder(cursor.getInt(0), cursor.getInt(1), cursor.getLong(2), cursor.getString(3));
            reminderRecyclerViewItemArrayList.add(reminder);
        }
    }

//    @OnClick(R.id.buttonDeleteVisitReminder)
    public void deleteReminderFromSystemCalendar(int id) {
//        ContentResolver cr = getActivity().getContentResolver();
//        ContentValues values = new ContentValues();
//        Uri deleteUri = null;
//        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
//        int rows = getActivity().getContentResolver().delete(deleteUri, null, null);

        int arrayListId = id - 1;

        long calendarEventId = Long.parseLong
                (String.valueOf(reminderRecyclerViewItemArrayList.get(arrayListId).getCalendarId()));

        Toast.makeText(getActivity(), "KASOWANIE WIZYTY", Toast.LENGTH_LONG).show();

        Uri deleteUri;

//        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(eventID)));

        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calendarEventId);


        int rows = getActivity().getContentResolver().delete(deleteUri, null, null);

        if (rows>0)
            deleteReminderFromDatabase();
    }

    private void deleteReminderFromDatabase() {
        boolean ifDeleted = myDatabaseHelper.deleteReminderData(reminderRecyclerViewItemArrayList.get(0).getId());

        if (ifDeleted)
            Toast.makeText(getActivity(), "WIZYTA SKASOWANA!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity(), "WIZYTA NIE ZOSTAŁA SKASOWANA! SPRÓBUJ PONOWNIE", Toast.LENGTH_LONG).show();
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
                    setEventAndReminderOnSystemCalendar();
                    saveReminderToDatabase();
                    customRefreshRecyclerView();
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

    private void setEventAndReminderOnSystemCalendar() {
        ContentValues values = new ContentValues();

        dateString = String.valueOf(day) + "." + String.valueOf(month+1) + "."
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

    private void saveReminderToDatabase() {

        reminderObject = new Reminder();
        reminderObject.setVisitId(idMedicalVisit);
        reminderObject.setCalendarId(eventID);
        reminderObject.setDate(dateString);

        boolean isInserted = myDatabaseHelper.insertDataIntoReminderTable(reminderObject);

//        if (isInserted == true)
//            Toast.makeText(getActivity(), "Dane zapisane", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(getActivity(), "Dane nie zostały zapisane", Toast.LENGTH_LONG).show();

    }

    public void deleteReminder(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        idReminder = bundle.getInt("idObjectToDelete");
        showDialogToConfirmDeleteOperation();
    }

    public void showDialogToConfirmDeleteOperation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Czy chcesz usunąć przypomnienie?");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_delete_view, null);
        builder.setView(myView);
        builder.setNegativeButton("NIE",null);
        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabaseHelper.deleteReminderData(idReminder);
                deleteReminderFromSystemCalendar(idReminder);
                // wywołanie dwóch poniższych metod spowoduje odświeżenie widoku
                customRefreshRecyclerView();
            }
        });
        builder.show();
    }

    private void createAndSetContentAdapter() {
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setPadding(0, 0, 0, (int) getActivity().getResources().getDimension(R.dimen.md_keylines));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        // Set numbers of List in RecyclerView.
        private int LENGTH = 0;
        private ArrayList<Reminder> reminderCardViewItemArrayList = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public Button button;
            public ImageButton deleteButton;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.list_records_fragment_card_item, parent, false));
                button = (Button) itemView.findViewById(R.id.buttonRecordOnList);
                deleteButton = (ImageButton) itemView.findViewById(R.id.buttonDeleteRecord);

            }
        }

        public ContentAdapter(Context context) {

            reminderCardViewItemArrayList = reminderRecyclerViewItemArrayList;

            this.LENGTH = reminderCardViewItemArrayList.size();
        }

        @Override
        public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }


        /**
         * Specify the contents of each item of the RecyclerView.
         */
        @Override
        public void onBindViewHolder(ContentAdapter.ViewHolder holder, int position) {
            String tempString = "";

            tempString += reminderCardViewItemArrayList.get(position % reminderCardViewItemArrayList.size()).getDate();
            holder.button.setText(tempString);

            // nadawane jest takie samo ID dla przycisku wyboru jak i usuwania wizyty
            int id = reminderCardViewItemArrayList.get(position % reminderCardViewItemArrayList.size()).getId();

            holder.button.setTag(id);
            holder.deleteButton.setTag(id);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
