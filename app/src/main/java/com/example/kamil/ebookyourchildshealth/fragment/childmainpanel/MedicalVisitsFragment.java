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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.AddObjectActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.VisitListItem;
import com.example.kamil.ebookyourchildshealth.util.Util;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MedicalVisitsFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private MyDatabaseHelper myDatabaseHelper;
    private Intent intent;
    private int childIDFromIntent;
    private String childNameFromIntent;
    private static Context context;
    private static ArrayList<VisitListItem> visitListItemObjectsArrayList;

    @BindString(R.string.fragment_decision_visit)
    String fragmentDecisionVisit;

    @BindView(R.id.recycler_view_medical_visits)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_visits, container, false);

        ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity()); // activity czy context???
        context = getActivity();

        getBundleFromIntent();
        getVisitDataFromDatabase();
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

    @OnClick(R.id.buttonAddMedicalVisit)
    public void newActivityAddMedicalVisitActivity() {
        intent = new Intent(this.getActivity(), AddObjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentDecision",fragmentDecisionVisit);
        bundle.putInt("childIDFromIntent", childIDFromIntent);
        bundle.putString("childNameFromIntent", childNameFromIntent);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Util.RESULT_CODE) {
            getVisitDataFromDatabase();
            createAndSetContentAdapter();
        }
    }

    public void newActivityGoToInfoMedicalVisitActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void deleteMedicalVisit(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        int idMedicalVisit = bundle.getInt("idObjectToDelete");
        showDialogToConfirmDeleteOperation(idMedicalVisit);
    }

    public void showDialogToConfirmDeleteOperation(int idMedicalVisit) {
        final int visitID = idMedicalVisit;

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Czy chcesz usunąć wizytę?");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_view, null);
        final EditText editTextDialog = (EditText) myView.findViewById(R.id.text_view_dialog);
        editTextDialog.setFocusable(false);
        editTextDialog.setClickable(false);
        builder.setView(myView);
        builder.setNegativeButton("NIE",null);
        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabaseHelper.deleteMedicalVisitData(visitID);
                // wywołanie dwóch poniższych metod spowoduje odświeżenie widoku
                getVisitDataFromDatabase();
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

    public void getVisitDataFromDatabase() {
        visitListItemObjectsArrayList = new ArrayList<>();
        VisitListItem visitListItem;

        Cursor cursor = myDatabaseHelper.readAllChildMedicalVisitsData(childIDFromIntent);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            visitListItem = new VisitListItem(cursor.getInt(0), cursor.getString(2), cursor.getString(5));
            visitListItemObjectsArrayList.add(visitListItem);
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

        private ArrayList<VisitListItem> visitListItemObjectsCardViewItem = new ArrayList<>();

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
            Resources resources = context.getResources();

            visitListItemObjectsCardViewItem = visitListItemObjectsArrayList;

            this.LENGTH = visitListItemObjectsCardViewItem.size();
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
            tempString += visitListItemObjectsCardViewItem.get(position % visitListItemObjectsCardViewItem.size()).getName()
                    + "  -  " + visitListItemObjectsCardViewItem.get(position % visitListItemObjectsCardViewItem.size()).getDate();
            holder.button.setText(tempString);
            // nadawane jest takie samo ID dla przycisku wyboru jak i usuwania wizyty
            int id = visitListItemObjectsCardViewItem.get(position % visitListItemObjectsCardViewItem.size()).getId();
            holder.button.setTag(id);
            holder.deleteButton.setTag(id);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
