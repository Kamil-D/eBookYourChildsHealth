package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Visit;
import com.example.kamil.ebookyourchildshealth.util;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AddMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;
    private Unbinder unbinder;
    private String[] textViewNamesArray;
    private int day, month, year;
    private Calendar calendar;
    private int childIDfromIntent;
    private String childNameFromIntent;
    private Visit visitObject;

    private TextView textViewName;
    private TextView textViewDoctor;
    private TextView textViewDisease;
    private TextView textViewDate;
    private TextView textViewDescription;
    private TextView textViewRecommendations;
    private TextView textViewMedicines;

    private EditText editTextName;
    private EditText editTextDoctor;
    private Spinner spinnerDisease;
    private Button buttonVisitDate;
    private EditText editTextDescription;
    private EditText editTextRecommendations;
    private EditText editTextMedicines;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medical_visit, container, false);
        myDebugger = new MyDebugger();
        unbinder = ButterKnife.bind(this, view);
        myDatabaseHelper = new MyDatabaseHelper(getActivity()); // activity czy context???

        getChildNameFromIntent();
        getChildIDFromIntent();
        setArrayContainsTextViewNames();
        createTextView(view);
        createEditText(view);
        createAndSetSpinners(view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }

    private void getChildNameFromIntent() {
        childNameFromIntent = getActivity().getIntent().getStringExtra("childNameFromIntent");
    }

    private void getChildIDFromIntent() {
        int defaultValue = 0;
        childIDfromIntent = getActivity().getIntent().getIntExtra("childIDFromIntent", defaultValue);
    }

    private void setArrayContainsTextViewNames() {
        Resources resources = getActivity().getResources();
        textViewNamesArray = resources.getStringArray(R.array.visit_table);
    }

    private void createTextView(View view) {
        textViewName = (TextView) view.findViewById(R.id.columnVisitName);
        textViewDoctor = (TextView) view.findViewById(R.id.columnDoctor);
        textViewDisease = (TextView) view.findViewById(R.id.columnDisease);
        textViewDate = (TextView) view.findViewById(R.id.columnDate);
        textViewDescription = (TextView) view.findViewById(R.id.columnDescription);
        textViewRecommendations = (TextView) view.findViewById(R.id.columnRecommendations);
        textViewMedicines = (TextView) view.findViewById(R.id.columnMedicines);

        textViewName.setText(textViewNamesArray[0].toString());
        textViewDoctor.setText(textViewNamesArray[1].toString());
        textViewDisease.setText(textViewNamesArray[2].toString());
        textViewDate.setText(textViewNamesArray[3].toString());
        textViewDescription.setText(textViewNamesArray[4].toString());
        textViewRecommendations.setText(textViewNamesArray[5].toString());
        textViewMedicines.setText(textViewNamesArray[6].toString());
    }

    private void createEditText(View view) {
        editTextName = (EditText) view.findViewById(R.id.columnVisitNameValue);
        editTextDoctor = (EditText) view.findViewById(R.id.columnDoctorValue);
        spinnerDisease = (Spinner) view.findViewById(R.id.columnDiseaseValueSpinner);
        buttonVisitDate = (Button) view.findViewById(R.id.buttonDatePicker);
        editTextDescription = (EditText) view.findViewById(R.id.columnDescriptionValue);
        editTextRecommendations = (EditText) view.findViewById(R.id.columnRecommendationsValue);
        editTextMedicines = (EditText) view.findViewById(R.id.columnMedicinesValue);
    }

    private void createAndSetSpinners(View view) {
        spinnerDisease = (Spinner) view.findViewById(R.id.columnDiseaseValueSpinner);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_diseases_array, android.R.layout.simple_spinner_item);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDisease.setAdapter(adapterSpinner);
    }

    @OnClick(R.id.buttonSaveVisit)
    public void saveChildToDatabaseButtonAction(View v) {
        visitObject = new Visit();

        if (checkIfAllFieldAreFilled()) {
            visitObject.setChild_id(childIDfromIntent);
            visitObject.setName(editTextName.getText().toString());
            visitObject.setDoctor(editTextDoctor.getText().toString());
            visitObject.setDisease(spinnerDisease.getSelectedItem().toString());
            visitObject.setDate(buttonVisitDate.getText().toString());
            visitObject.setDescription(editTextDescription.getText().toString());
            visitObject.setRecommendations(editTextRecommendations.getText().toString());
            visitObject.setMedicines(editTextMedicines.getText().toString());

            boolean isInserted = myDatabaseHelper.insertDataIntoVisitTable(visitObject);

            if (isInserted == true)
                Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Data not inserted", Toast.LENGTH_LONG).show();

            getActivity().setResult(util.RESULT_CODE, null);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "COMPLETE ALL FIELDS!", Toast.LENGTH_LONG).show();
    }

    private boolean checkIfAllFieldAreFilled() {
        if (editTextName.getText().toString().matches("") ||
                editTextName.getText().toString().matches("") ||
                editTextDoctor.getText().toString().matches("") ||
                spinnerDisease.getSelectedItem().toString().matches("") ||
                buttonVisitDate.getText().toString().matches("") ||
                editTextDescription.getText().toString().matches("") ||
                editTextRecommendations.getText().toString().matches("") ||
                editTextMedicines.getText().toString().matches(""))
            return false;
        return true;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) // +1 bo miesiące numeruje od 0
                            + "/" +String.valueOf(year);
                    setDateOnButton(date);
                }
            };

    private void setDateOnButton(String date) {
        buttonVisitDate.setText(date);
    }

    @OnClick(R.id.buttonDatePicker)
    public void showDatePickerDialog(View v) {
        setCurrentDate();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,datePickerListener,
                year, month, day);
        datePickerDialog.show();
    }

    private void setCurrentDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

}
