package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Disease;
import com.example.kamil.ebookyourchildshealth.model.Note;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kamil on 2016-12-18.
 */

public class InfoDiseaseTabTwoFragment extends Fragment {

    static MyDebugger myDebugger = new MyDebugger();
    private MyDatabaseHelper myDatabaseHelper;
    private String[] textViewLeftColumnNamesArray;
    private int idDisease;
    private int childIDFromIntent;
    private int idNote;
    private Note noteObject;
    private static ArrayList<Note> noteRecyclerViewItemArrayList;
    private static Context context;

    @BindString(R.string.pick_date)
    String pickDateString;

    @BindView(R.id.recycler_view_diseases)
    RecyclerView recyclerView;

    @BindView(R.id.columnNoteMessage)
    TextView textViewNoteMessage;

    @BindView(R.id.columnNoteMessageValue)
    EditText editTextNoteMessage;

    @BindView(R.id.buttonAddNote)
    Button buttonAddNote;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_disease_tab_two, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());
        context = getActivity();

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
        setTextOnLeftColumnTextView();
        getDiseaseNoteDataFromDatabase();
        createAndSetContentAdapter();

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
////        Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG).show();
//        Toast toast = Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG);
//        ViewGroup group = (ViewGroup) toast.getView();
//        TextView messageTextView = (TextView) group.getChildAt(0);
//        messageTextView.setTextSize(25);
//        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 70);
//        toast.show();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }

    private void getBundleFromIntent() {
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        idDisease = bundle.getInt("idObjectToShow");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
    }

    private void setTextOnLeftColumnTextView() {
        Resources resources = getContext().getResources();
        textViewLeftColumnNamesArray = resources.getStringArray(R.array.disease_table);
        textViewNoteMessage.setText(R.string.text_view_add_note);
    }

    public void getDiseaseNoteDataFromDatabase() {
        noteRecyclerViewItemArrayList = new ArrayList<>();
        Note note;

        Cursor cursor = myDatabaseHelper.readSingleDiseaseNotesData(idDisease);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            note = new Note(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3));
            noteRecyclerViewItemArrayList.add(note);
        }

    }

    private String getCurrentDate() {
        String dateString;
        int day, month, year;
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dateString = String.valueOf(day) + "/" + String.valueOf(month) + "/" +String.valueOf(year);

        return dateString;
    }

    private void createAndSetContentAdapter() {
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setPadding(0, 0, 0, (int) getActivity().getResources().getDimension(R.dimen.md_keylines));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public static Context getAppContext(){
        return context;
    }


    @OnClick(R.id.buttonAddNote)
    public void saveDiseaseNoteToDatabaseButtonAction(View v) {

        noteObject = new Note();

        if (!editTextNoteMessage.getText().toString().matches("")) {
            noteObject.setDiseaseId(idDisease);
            noteObject.setNoteText(editTextNoteMessage.getText().toString());
            noteObject.setDate(getCurrentDate());

            boolean isInserted = myDatabaseHelper.insertDataIntoNotesTable(noteObject);

            if (isInserted == true)
                Toast.makeText(getActivity(), "Dane zapisane", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Dane nie zostały zapisane", Toast.LENGTH_LONG).show();

            customRefreshRecyclerView();
            editTextNoteMessage.setText("");
//            getActivity().setResult(UtilCode.RESULT_CODE, null);
//            getActivity().finish();
        } else
            Toast.makeText(getActivity(), "UZUPEŁNIJ POLE!", Toast.LENGTH_LONG).show();
    }

    private void customRefreshRecyclerView() {
        getDiseaseNoteDataFromDatabase();
        createAndSetContentAdapter();
    }

    public void deleteNote(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        idNote = bundle.getInt("idObjectToDelete");
        showDialogToConfirmDeleteOperation();
    }

    public void showDialogToConfirmDeleteOperation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Czy chcesz usunąć notatkę?");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_delete_view, null);
        builder.setView(myView);
        builder.setNegativeButton("NIE",null);
        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabaseHelper.deleteDiseaseNoteData(idNote);
                // wywołanie dwóch poniższych metod spowoduje odświeżenie widoku
                getDiseaseNoteDataFromDatabase();
                createAndSetContentAdapter();
            }
        });
        builder.show();
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        // Set numbers of List in RecyclerView.
        private int LENGTH = 0;

        private ArrayList<Note> noteCardViewItem = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewNoteDate;
            public ImageButton buttonDelete;
            public TextView textViewNoteText;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.list_notes_fragment_card_item, parent, false));
                textViewNoteText = (TextView) itemView.findViewById(R.id.noteText);
                textViewNoteDate = (TextView) itemView.findViewById(R.id.noteDate);
                buttonDelete = (ImageButton) itemView.findViewById(R.id.buttonDeleteNote);
            }
        }

        public ContentAdapter(Context context) {

            noteCardViewItem = noteRecyclerViewItemArrayList;

            this.LENGTH = noteCardViewItem.size();
        }

        @Override
        public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }


        /**
         * Specify the contents of each item of the RecyclerView.
         */
        @Override
        public void onBindViewHolder(final ContentAdapter.ViewHolder holder, int position) {
            String tempString =  noteCardViewItem.get(position % noteCardViewItem.size()).getNoteText();
            holder.textViewNoteDate.setText(noteCardViewItem.get(position % noteCardViewItem.size()).getDate());
            holder.textViewNoteText.setText(tempString);
            holder.textViewNoteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.textViewNoteText.getMaxLines() == 1)
                        holder.textViewNoteText.setMaxLines(10);
                    else
                        holder.textViewNoteText.setMaxLines(1);
                }
            });
            // nadawane jest takie samo ID dla przycisku wyboru jak i usuwania wizyty
            int id = noteCardViewItem.get(position % noteCardViewItem.size()).getId();
            holder.textViewNoteText.setTag(R.integer.tagNoteId, id);
            holder.buttonDelete.setTag(id);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
