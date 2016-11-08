package com.example.kamil.ebookyourchildshealth.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
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

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.ChooseChildMainActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Child;

import java.util.Calendar;


public class AddNewChildFragment extends Fragment {

    static MyDebugger myDebugger;

    private Intent intent;
    private String[] textViewNamesArray;
    private MyDatabaseHelper myDatabaseHelper;
    private Button saveChildButton;
    private Unbinder unbinder;

    private int day, month, year;
    private Calendar calendar;

    private Child childObject;

    private TextView textViewName;
    private TextView textViewSurname;
    private TextView textViewPesel;
    private TextView textViewSex;
    private TextView textViewBlood;
    private TextView textViewBirthDate;
    private TextView textViewBirthPlace;
    private TextView textViewMother;
    private TextView textViewFather;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPesel;
    private Spinner spinnerSex;
    private Spinner spinnerBlood;
    private Button buttonBirthDate;
    private EditText editTextBirthPlace;
    private EditText editTextMother;
    private EditText editTextFather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_child, container, false);

        myDebugger = new MyDebugger();
        unbinder = ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity()); // activity czy context???
        saveChildButton = (Button) view.findViewById(R.id.buttonSaveChild);

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

    private void setArrayContainsTextViewNames () {
        Resources resources = getActivity().getResources();
        textViewNamesArray = resources.getStringArray(R.array.child_table);
    }

    private void createTextView(View view) {
        textViewName = (TextView) view.findViewById(R.id.columnName1);
        textViewSurname = (TextView) view.findViewById(R.id.columnSurname);
        textViewPesel = (TextView) view.findViewById(R.id.columnPesel);
        textViewSex = (TextView) view.findViewById(R.id.columnSex);
        textViewBlood = (TextView) view.findViewById(R.id.columnBloodGroup);
        textViewBirthDate = (TextView) view.findViewById(R.id.columnBirthDate);
        textViewBirthPlace = (TextView) view.findViewById(R.id.columnBirthPlace);
        textViewMother = (TextView) view.findViewById(R.id.columnMother);
        textViewFather = (TextView) view.findViewById(R.id.columnFather);

        textViewName.setText(textViewNamesArray[0].toString());
        textViewSurname.setText(textViewNamesArray[1].toString());
        textViewPesel.setText(textViewNamesArray[2].toString());
        textViewSex.setText(textViewNamesArray[3].toString());
        textViewBlood.setText(textViewNamesArray[4].toString());
        textViewBirthDate.setText(textViewNamesArray[5].toString());
        textViewBirthPlace.setText(textViewNamesArray[6].toString());
        textViewMother.setText(textViewNamesArray[7].toString());
        textViewFather.setText(textViewNamesArray[8].toString());
    }

    private void createEditText(View view) {
        editTextName = (EditText) view.findViewById(R.id.columnNameValue);
        editTextSurname = (EditText) view.findViewById(R.id.columnSurnameValue);
        editTextPesel = (EditText) view.findViewById(R.id.columnPeselValue);
        spinnerSex = (Spinner) view.findViewById(R.id.columnSexValueSpinner);
        spinnerBlood = (Spinner) view.findViewById(R.id.columnBloodGroupValueSpinner);
        buttonBirthDate = (Button) view.findViewById(R.id.buttonDatePicker);
        editTextBirthPlace = (EditText) view.findViewById(R.id.columnBirthPlaceValue);
        editTextMother = (EditText) view.findViewById(R.id.columnMotherValue);
        editTextFather = (EditText) view.findViewById(R.id.columnFatherValue);
    }

    private void createAndSetSpinners(View view) {
        spinnerSex = (Spinner) view.findViewById(R.id.columnSexValueSpinner);
        spinnerBlood = (Spinner) view.findViewById(R.id.columnBloodGroupValueSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        /**An ArrayAdapter is an adapter backed by an array of objects.
         * It links the array to the Adapter View.
         * The default ArrayAdapter converts an array item into a String object
         * putting it into a TextView. The text view is then displayed
         * in the AdapterView (a ListView for example).
         * When you create the adapter, you need to supply the layout for displaying each array string.
         * You can define your own or use one of Android’s, such as:
         */
        ArrayAdapter<CharSequence> adapterSpinner1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_sex_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterSpinner2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_blood_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSex.setAdapter(adapterSpinner1);
        spinnerBlood.setAdapter(adapterSpinner2);
    }

    @OnClick(R.id.buttonSaveChild)
    public void saveChildToDatabaseButtonAction(View v) {
        childObject = new Child();

        if (checkIfAllFieldAreFilled()) {

            childObject.setName(editTextName.getText().toString());
            childObject.setSurname(editTextSurname.getText().toString());
            childObject.setPesel(editTextPesel.getText().toString());
            childObject.setSex(spinnerSex.getSelectedItem().toString());
            childObject.setBlood_group(spinnerBlood.getSelectedItem().toString());
            childObject.setBirth_date(buttonBirthDate.getText().toString());
            childObject.setBirth_place(editTextBirthPlace.getText().toString());
            childObject.setMother(editTextMother.getText().toString());
            childObject.setFather(editTextFather.getText().toString());

            boolean isInserted = myDatabaseHelper.insertDataIntoChildTable(childObject);

            if (isInserted == true)
                Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Data not inserted", Toast.LENGTH_LONG).show();

            newActivityBackToChooseChildMainActivity();
        } else
            Toast.makeText(getActivity(), "COMPLETE ALL FIELDS!", Toast.LENGTH_LONG).show();
    }

    private boolean checkIfAllFieldAreFilled() {
        if (editTextName.getText().toString().matches("") ||
                editTextSurname.getText().toString().matches("") ||
                editTextPesel.getText().toString().matches("") ||
                spinnerSex.getSelectedItem().toString().matches("") ||
                spinnerBlood.getSelectedItem().toString().matches("") ||
                buttonBirthDate.getText().toString().matches("") ||
                editTextBirthPlace.getText().toString().matches("") ||
                editTextMother.getText().toString().matches("") ||
                editTextFather.getText().toString().matches(""))
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
        buttonBirthDate.setText(date);
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

    private void newActivityBackToChooseChildMainActivity () {
        intent = new Intent(this.getActivity(), ChooseChildMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
