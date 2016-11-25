package com.example.kamil.ebookyourchildshealth.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.ChooseChildMainActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Child;
import com.example.kamil.ebookyourchildshealth.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class AddNewChildFragment extends Fragment {

    static MyDebugger myDebugger;
    private String[] textViewLeftColumnNamesArray;
    private MyDatabaseHelper myDatabaseHelper;
    private Button saveChildButton;
    private Bitmap croppedImage;
    private Calendar calendar;
    private int day, month, year;
    private Child childObject;
    private Uri uriChildPhoto;

    @BindString(R.string.pick_date)
    String pickDateString;

    @BindView(R.id.imageButtonAddPhoto)
    ImageButton imageButton;

    @BindView(R.id.columnName1)
    TextView textViewName;

    @BindView(R.id.columnSurname)
    TextView textViewSurname;

    @BindView(R.id.columnPesel)
    TextView textViewPesel;

    @BindView(R.id.columnSex)
    TextView textViewSex;

    @BindView(R.id.columnBloodGroup)
    TextView textViewBlood;

    @BindView(R.id.columnBirthDate)
    TextView textViewBirthDate;

    @BindView(R.id.columnBirthPlace)
    TextView textViewBirthPlace;

    @BindView(R.id.columnMother)
    TextView textViewMother;

    @BindView(R.id.columnFather)
    TextView textViewFather;

    @BindView(R.id.columnNameValue)
    EditText editTextName;

    @BindView(R.id.columnSurnameValue)
    EditText editTextSurname;

    @BindView(R.id.columnPeselValue)
    EditText editTextPesel;

    @BindView(R.id.columnSexValueSpinner)
    Spinner spinnerSex;

    @BindView(R.id.columnBloodGroupValueSpinner)
    Spinner spinnerBlood;

    @BindView(R.id.buttonDatePicker)
    Button buttonBirthDate;

    @BindView(R.id.columnBirthPlaceValue)
    EditText editTextBirthPlace;

    @BindView(R.id.columnMotherValue)
    EditText editTextMother;

    @BindView(R.id.columnFatherValue)
    EditText editTextFather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_child, container, false);

        myDebugger = new MyDebugger();
        ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());
        saveChildButton = (Button) view.findViewById(R.id.buttonSaveChild);

        setArrayContainsTextViewNames();
        setTextOnLeftColumnTextView();
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
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.child_table);
    }

    private void setTextOnLeftColumnTextView() {
        textViewName.setText(textViewLeftColumnNamesArray[0]);
        textViewSurname.setText(textViewLeftColumnNamesArray[1]);
        textViewPesel.setText(textViewLeftColumnNamesArray[2]);
        textViewSex.setText(textViewLeftColumnNamesArray[3]);
        textViewBlood.setText(textViewLeftColumnNamesArray[4]);
        textViewBirthDate.setText(textViewLeftColumnNamesArray[5]);
        textViewBirthPlace.setText(textViewLeftColumnNamesArray[6]);
        textViewMother.setText(textViewLeftColumnNamesArray[7]);
        textViewFather.setText(textViewLeftColumnNamesArray[8]);
    }

    private void createAndSetSpinners(View view) {
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
                R.array.spinner_sex_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterSpinner2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_blood_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSex.setAdapter(adapterSpinner1);
        spinnerBlood.setAdapter(adapterSpinner2);
    }

    public void setImageOnImageButton(String uri, Uri resultUri) throws IOException {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(uri, imageButton);
        this.uriChildPhoto = resultUri;  // kopiujemy uri obrazka do zmiennej klasy, którą wrzucimy do bd
        croppedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
        myDebugger.someMethod("!!!!!!!!!!!! ADRES FOTKI: " + uriChildPhoto.toString());
    }

    @OnClick(R.id.imageButtonAddPhoto)
    public void pickPhoto() {
//        IntentHelper.chooseFileIntent(this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  // nowy intent i ustawia typ na pobranie plików
        intent.setType("image/*");  // typ pliku
        this.startActivityForResult(intent, Util.FILE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.FILE_PICK_CODE && resultCode == RESULT_OK) {

            Uri imageUri;
            imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(15,9).setFixAspectRatio(true)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    setImageOnImageButton("file://" + resultUri.getPath(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.buttonSaveChild)
    public void saveChildToDatabaseButtonAction(View v) {
        childObject = new Child();

        if (checkIfAllFieldAreFilled()) {
//        if (true) {
            if (checkIfPeselCorrect()) {
//                if (true) {
                childObject.setName(editTextName.getText().toString());
                childObject.setSurname(editTextSurname.getText().toString());
                childObject.setPesel(editTextPesel.getText().toString());
                childObject.setSex(spinnerSex.getSelectedItem().toString());
                childObject.setBloodGroup(spinnerBlood.getSelectedItem().toString());
                childObject.setBirthDate(buttonBirthDate.getText().toString());
                childObject.setBirthPlace(editTextBirthPlace.getText().toString());
                childObject.setMother(editTextMother.getText().toString());
                childObject.setFather(editTextFather.getText().toString());
                childObject.setImageUri(uriChildPhoto);

                boolean isInserted = myDatabaseHelper.insertDataIntoChildTable(childObject);

                if (isInserted == true) {
                    Toast.makeText(getActivity(), "Dane zapisane", Toast.LENGTH_LONG).show();
                    newActivityBackToChooseChildMainActivity();
                }
                else
                    Toast.makeText(getActivity(), "Dane nie zostały zapisane", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getActivity(), "NIEPOPRAWNY PESEL!", Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(getActivity(), "UZUPEŁNIJ WSZYSTKIE POLA!", Toast.LENGTH_LONG).show();
    }

    private boolean checkIfAllFieldAreFilled() {
        String uriString = "";
        if (uriChildPhoto != null)
            uriString += uriChildPhoto.toString();

        if (uriString.matches("") ||
                editTextName.getText().toString().matches("") ||
                editTextSurname.getText().toString().matches("") ||
                editTextPesel.getText().toString().matches("") ||
                spinnerSex.getSelectedItem().toString().matches("") ||
                spinnerBlood.getSelectedItem().toString().matches("") ||
                buttonBirthDate.getText().toString().matches(pickDateString) ||
                editTextBirthPlace.getText().toString().matches("") ||
                editTextMother.getText().toString().matches("") ||
                editTextFather.getText().toString().matches(""))
            return false;
        return true;
    }

    private boolean checkIfPeselCorrect() {
        char tempChar;
        int charCount = 0;
        String string = editTextPesel.getText().toString();

        for (int i=0 ; i<string.length() ; i++ ) {
            tempChar = string.charAt(i);
            if (tempChar<'0' && tempChar>'9')
                return false;
            charCount++;
        }

        if (charCount!=11)
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
        Intent intent = new Intent(this.getActivity(), ChooseChildMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
