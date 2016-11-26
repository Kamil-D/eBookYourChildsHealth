package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.AddObjectActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.DiseaseListItem;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kamil on 2016-11-24.
 */

public class DiseasesFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDatabaseHelper;
    private Intent intent;
    private int childIDFromIntent;
    private String childNameFromIntent;
    private int idDiseaseToDelete;
    private static Context context;
    private static ArrayList<DiseaseListItem> diseaseListItemObjectsArrayList;

    @BindString(R.string.fragment_decision_disease)
    String fragmentDecisionDisease;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diseases, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_diseases);
        ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity()); // activity czy context???
        context = getActivity();

        getBundleFromIntent();
        getDiseaseDataFromDatabase();
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

    @OnClick(R.id.buttonAddDisease)
    public void newActivityAddDiseaseActivity() {
        intent = new Intent(this.getActivity(), AddObjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentDecision", fragmentDecisionDisease);
        bundle.putInt("childIDFromIntent", childIDFromIntent);
        bundle.putString("childNameFromIntent", childNameFromIntent);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== UtilCode.RESULT_CODE) {
            getDiseaseDataFromDatabase();
            createAndSetContentAdapter();
        }
    }

    public void newActivityGoToInfoDiseaseActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void deleteMedicalVisit(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        idDiseaseToDelete = bundle.getInt("idObjectToDelete");
        showDialogToConfirmDeleteOperation();
    }

    public void showDialogToConfirmDeleteOperation() {
        //final int diseaseID = idDiseaseToDelete;

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Czy chcesz usunąć chorobę?");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_view, null);
        final EditText editTextDialog = (EditText) myView.findViewById(R.id.text_view_dialog);
        editTextDialog.setFocusable(false);
        editTextDialog.setClickable(false);
        builder.setView(myView);
        builder.setNegativeButton("NIE",null);
        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabaseHelper.deleteDiseaseNoteData(idDiseaseToDelete);
                myDatabaseHelper.deleteDiseaseData(idDiseaseToDelete);
                // wywołanie dwóch poniższych metod spowoduje odświeżenie widoku
                getDiseaseDataFromDatabase();
                createAndSetContentAdapter();
            }
        });
        builder.show();
    }

    public static Context getAppContext(){
        return context;
    }

    private void getBundleFromIntent() {
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
        childNameFromIntent = bundle.getString("childNameFromIntent");
    }

    public void getDiseaseDataFromDatabase() {
        diseaseListItemObjectsArrayList = new ArrayList<>();
        DiseaseListItem disease;

        Cursor cursor = myDatabaseHelper.readAllChildDiseasesData(childIDFromIntent);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            disease = new DiseaseListItem(cursor.getInt(0), cursor.getString(2), cursor.getString(3));
            diseaseListItemObjectsArrayList.add(disease);
        }

    }

    private void createAndSetContentAdapter() {
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        // Set numbers of List in RecyclerView.
        private int LENGTH = 0;
        private ArrayList<DiseaseListItem> diseaseListItemObjectsCardViewItem = new ArrayList<>();

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

            diseaseListItemObjectsCardViewItem = diseaseListItemObjectsArrayList;

            this.LENGTH = diseaseListItemObjectsCardViewItem.size();
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
            tempString += diseaseListItemObjectsCardViewItem.get(position % diseaseListItemObjectsCardViewItem.size()).getName()
                    + "  -  " + diseaseListItemObjectsCardViewItem.get(position % diseaseListItemObjectsCardViewItem.size()).getDate();
            holder.button.setText(tempString);
            // nadawane jest takie samo ID dla przycisku wyboru jak i usuwania wizyty
            int id = diseaseListItemObjectsCardViewItem.get(position % diseaseListItemObjectsCardViewItem.size()).getId();
            holder.button.setTag(id);
            holder.deleteButton.setTag(id);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
