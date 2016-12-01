package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.kamil.ebookyourchildshealth.model.DiseaseListItem;
import com.example.kamil.ebookyourchildshealth.model.Visit;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private MyDatabaseHelper myDatabaseHelper;
    private String[] textViewLeftColumnNamesArray;
    private int day, month, year;
    private Calendar calendar;
    private int childIDFromIntent;
    private Visit visitObject;
    private static ArrayList<DiseaseListItem> diseaseListItemObjectsArrayList;
    private static ArrayList<String> diseasesNameDateArrayList;
    private Bundle bundle;
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
    EditText editTextName;

    @BindView(R.id.columnDoctorValue)
    EditText editTextDoctor;

    @BindView(R.id.columnDiseaseValueSpinner)
    Spinner spinnerDisease;

    @BindView(R.id.buttonDatePicker)
    Button buttonVisitDate;

    @BindView(R.id.columnDescriptionValue)
    EditText editTextDescription;

    @BindView(R.id.columnRecommendationsValue)
    EditText editTextRecommendations;

    @BindView(R.id.columnMedicinesValue)
    EditText editTextMedicines;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medical_visit, container, false);

        myDebugger = new MyDebugger();
        ButterKnife.bind(this, view);
        myDatabaseHelper = new MyDatabaseHelper(getActivity());

        getBundleFromIntent();
        setArrayContainsTextViewNames();
        setTextOnLeftColumnTextView();
        createAndSetSpinners();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }

    private void getBundleFromIntent() {
        bundle = getActivity().getIntent().getBundleExtra("bundle");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
    }

    private void setArrayContainsTextViewNames() {
        Resources resources = getActivity().getResources();
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.visit_table);
    }

    private void setTextOnLeftColumnTextView() {
        textViewName.setText(textViewLeftColumnNamesArray[0]);
        textViewDoctor.setText(textViewLeftColumnNamesArray[1]);
        textViewDisease.setText(textViewLeftColumnNamesArray[2]);
        textViewDate.setText(textViewLeftColumnNamesArray[3]);
        textViewDescription.setText(textViewLeftColumnNamesArray[4]);
        textViewRecommendations.setText(textViewLeftColumnNamesArray[5]);
        textViewMedicines.setText(textViewLeftColumnNamesArray[6]);
    }

    private void createAndSetSpinners() {

        getDiseaseDataFromDatabase();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, diseasesNameDateArrayList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisease.setAdapter(adapterSpinner);

//        id aktualnie zaznaczonego obiektu
//        spinnerDisease.getSelectedItemPosition();
    }

    private void getDiseaseDataFromDatabase() {
        diseaseListItemObjectsArrayList = new ArrayList<>();
        diseasesNameDateArrayList = new ArrayList<>();
        DiseaseListItem disease;

        Cursor cursor = myDatabaseHelper.readAllChildDiseasesData(childIDFromIntent);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            disease = new DiseaseListItem(cursor.getInt(0), cursor.getString(2), cursor.getString(3));
            diseaseListItemObjectsArrayList.add(disease);
            diseasesNameDateArrayList.add(cursor.getString(2) + " - " + cursor.getString(3));
        }
    }

    @OnClick(R.id.buttonSaveVisit)
    public void saveVisitToDatabaseButtonAction(View v) {
        visitObject = new Visit();

        if (checkIfAllFieldAreFilled()) {
            visitObject.setChildId(childIDFromIntent);
            visitObject.setName(editTextName.getText().toString());
            visitObject.setDoctor(editTextDoctor.getText().toString());

            visitObject.setDiseaseId(diseaseListItemObjectsArrayList.get(spinnerDisease.getSelectedItemPosition()).getId());

            visitObject.setDate(buttonVisitDate.getText().toString());
            visitObject.setDescription(editTextDescription.getText().toString());
            visitObject.setRecommendations(editTextRecommendations.getText().toString());
            visitObject.setMedicines(editTextMedicines.getText().toString());

            boolean isInserted = myDatabaseHelper.insertDataIntoVisitTable(visitObject);

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
        if (editTextName.getText().toString().matches("") ||
                editTextName.getText().toString().matches("") ||
                editTextDoctor.getText().toString().matches("") ||
                buttonVisitDate.getText().toString().matches(pickDateString) ||
                editTextDescription.getText().toString().matches("") ||
                editTextRecommendations.getText().toString().matches("") ||
                editTextMedicines.getText().toString().matches(""))
            return false;
        return true;
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

}
