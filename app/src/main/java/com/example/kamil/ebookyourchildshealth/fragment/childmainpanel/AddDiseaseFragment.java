package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.example.kamil.ebookyourchildshealth.model.Disease;
import com.example.kamil.ebookyourchildshealth.model.Visit;
import com.example.kamil.ebookyourchildshealth.util.Util;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddDiseaseFragment extends Fragment {

    MyDebugger myDebugger;
    private MyDatabaseHelper myDatabaseHelper;
    private String[] textViewLeftColumnNamesArray;
    private int day, month, year;
    private Calendar calendar;
    private int childIDFromIntent;
    private Disease diseasetObject;
    private Bundle bundle;
    @BindString(R.string.pick_date)
    String pickDateString;

    @BindView(R.id.columnDiseaseName)
    TextView textViewName;

    @BindView(R.id.columnDiseaseNameValue)
    EditText editTextName;

    @BindView(R.id.columnDate)
    TextView textViewDate;

    @BindView(R.id.buttonDatePicker)
    Button buttonVisitDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_disease, container, false);

        myDebugger = new MyDebugger();
        ButterKnife.bind(this, view);
        myDatabaseHelper = new MyDatabaseHelper(getActivity());

        getBundleFromIntent();
        setArrayContainsTextViewNames();
        setTextOnLeftColumnTextView();

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
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.disease_table);
    }

    private void setTextOnLeftColumnTextView() {
        textViewName.setText(textViewLeftColumnNamesArray[0]);
        textViewDate.setText(textViewLeftColumnNamesArray[1]);
    }

    @OnClick(R.id.buttonSaveDisease)
    public void saveDiseaseToDatabaseButtonAction(View v) {
        diseasetObject = new Disease();

        if (checkIfAllFieldAreFilled()) {
            diseasetObject.setChildId(childIDFromIntent);
            diseasetObject.setName(editTextName.getText().toString());
            diseasetObject.setDate(buttonVisitDate.getText().toString());

            boolean isInserted = myDatabaseHelper.insertDataIntoDiseaseTable(diseasetObject);

            if (isInserted == true)
                Toast.makeText(getActivity(), "Dane zapisane", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Dane nie zostały zapisane", Toast.LENGTH_LONG).show();

            getActivity().setResult(Util.RESULT_CODE, null);
            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "UZUPEŁNIJ WSZYSTKIE POLA!", Toast.LENGTH_LONG).show();
    }

    private boolean checkIfAllFieldAreFilled() {
        if ( editTextName.getText().toString().matches("") ||
                buttonVisitDate.getText().toString().matches(pickDateString) )
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
