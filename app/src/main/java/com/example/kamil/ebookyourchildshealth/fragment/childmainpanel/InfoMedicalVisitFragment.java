package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Visit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InfoMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private MyDatabaseHelper myDatabaseHelper;
    private String[] textViewLeftColumnNamesArray;
    private int idMedicalVisit;
    private int childIDFromIntent;
    private ArrayList<String> queryResultArrayList;
    private String[] queryResultArray;
    private static Context context;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_medical_visit, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();
        queryResultArrayList = new ArrayList<>();

        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity()); // activity czy context???

        context = getActivity();

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
//        getChildNameFromIntent();
        getChildDataFromDatabase();
        setTextOnTextViewLeftColumn();
        setTextFromDBOnTextViewRightColumn();
        createListeners();

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
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        idMedicalVisit = bundle.getInt("idMedicalVisit");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
    }

    public void getChildDataFromDatabase() {
        Cursor cursor = myDatabaseHelper.readMedicalVisitData(idMedicalVisit);
        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            queryResultArrayList.add(cursor.getString(2));
            queryResultArrayList.add(cursor.getString(3));
            queryResultArrayList.add(cursor.getString(4));
            queryResultArrayList.add(cursor.getString(5));
            queryResultArrayList.add(cursor.getString(6));
            queryResultArrayList.add(cursor.getString(7));
            queryResultArrayList.add(cursor.getString(8));

            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_medical_visit_info);
            toolbar.setTitle(queryResultArrayList.get(3));

            queryResultArray = new String[queryResultArrayList.size()];
        }
    }

    private void setTextOnTextViewLeftColumn() {
        Resources resources = getContext().getResources();
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.visit_table);

        textViewName.setText(textViewLeftColumnNamesArray[0].toString());
        textViewDoctor.setText(textViewLeftColumnNamesArray[1].toString());
        textViewDisease.setText(textViewLeftColumnNamesArray[2].toString());
        textViewDate.setText(textViewLeftColumnNamesArray[3].toString());
        textViewDescription.setText(textViewLeftColumnNamesArray[4].toString());
        textViewRecommendations.setText(textViewLeftColumnNamesArray[5].toString());
        textViewMedicines.setText(textViewLeftColumnNamesArray[6].toString());
    }

    private void setTextFromDBOnTextViewRightColumn() {
        // wynik z bazy danych mamy w ArrayList dlatego zamieniamy na Array
        queryResultArray = queryResultArrayList.toArray(queryResultArray);

        textViewNameValue.setText(queryResultArray[0]);
        textViewDoctorValue.setText(queryResultArray[1]);
        textViewDiseaseValue.setText(queryResultArray[2]);
        textViewDateValue.setText(queryResultArray[3]);
        textViewDescriptionValue.setText(queryResultArray[4]);
        textViewRecommendationsValue.setText(queryResultArray[5]);
        textViewMedicinesValue.setText(queryResultArray[6]);
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
        builder.setTitle("input text");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_view, null);
        final EditText edit_dialog = (EditText) myView.findViewById(R.id.text_view_dialog);
        edit_dialog.setText(str);
        builder.setView(myView);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(edit_dialog.getText().toString());
            }
        });

        builder.show();
    }

    public static Context getAppContext(){
        return context;
    }

}