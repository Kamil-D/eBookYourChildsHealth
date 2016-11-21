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
import android.widget.TextView;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.AddMedicalVisitActivity;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.InfoMedicalVisitActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.util.util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MedicalVisitsFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDatabaseHelper;
    private Intent intent;
    private int childIDFromIntent;
    private String childNameFromIntent;
    private static Context context;
    private static ArrayList<Integer> queryResultIdArrayList;
    private static ArrayList<String> queryResultNamesArrayList;
    private static ArrayList<String> queryResultDatesArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_visits, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_medical_visits);
        ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity()); // activity czy context???
        context = getActivity();

        queryResultIdArrayList = new ArrayList<>();
        queryResultNamesArrayList = new ArrayList<>();
        queryResultDatesArrayList = new ArrayList<>();

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
        intent = new Intent(this.getActivity(), AddMedicalVisitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("childIDFromIntent", childIDFromIntent);
        bundle.putString("childNameFromIntent", childNameFromIntent);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== util.RESULT_CODE) {
            getVisitDataFromDatabase();
            createAndSetContentAdapter();
        }
    }

    public void newActivityGoToInfoMedicalVisitActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void deleteMedicalVisit(Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        int idMedicalVisit = bundle.getInt("idMedicalVisit");
        showDialogToChangeValue(idMedicalVisit);
    }

    public void showDialogToChangeValue(int idMedicalVisit) {
        final int visitID = idMedicalVisit;

        AlertDialog.Builder builder = new AlertDialog.Builder(getAppContext());
        builder.setTitle("Czy chcesz usunąć wizytę?");
        View myView = LayoutInflater.from(getAppContext()).inflate(R.layout.dialog_view, null);
        builder.setView(myView);
        builder.setNegativeButton("NIE",null);
        builder.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              myDatabaseHelper.deleteMedicalVisitData(visitID);
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
        queryResultIdArrayList = new ArrayList<>();
        queryResultNamesArrayList = new ArrayList<>();
        queryResultDatesArrayList = new ArrayList<>();

        Cursor cursor = myDatabaseHelper.readAllChildMedicalVisitsData(childIDFromIntent);

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            queryResultIdArrayList.add(cursor.getInt(0));
            queryResultNamesArrayList.add(cursor.getString(2));
            queryResultDatesArrayList.add(cursor.getString(5));
        }
//        StringBuffer stringBuffer = new StringBuffer();
//        while(cursor.moveToNext()) {
//            stringBuffer.append("ID :" + cursor.getString(0) + "\n");
//            queryResultIdArrayList.add(cursor.getInt(0));
//            queryResultNamesArrayList.add(cursor.getString(1));
//        }
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

        private String[] visitNamesArrayButtonFromCardView = new String[queryResultNamesArrayList.size()];
        private String[] visitDatesArrayButtonFromCardView = new String[queryResultDatesArrayList.size()];

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public Button button;
            public ImageButton deleteButton;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.medical_visits_fragment_card_item, parent, false));
                button = (Button) itemView.findViewById(R.id.buttonMedicalVisitsList);
                deleteButton = (ImageButton) itemView.findViewById(R.id.buttonDeleteVisit);

            }
        }

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();

            visitNamesArrayButtonFromCardView = queryResultNamesArrayList.toArray(visitNamesArrayButtonFromCardView);
            visitDatesArrayButtonFromCardView = queryResultDatesArrayList.toArray(visitDatesArrayButtonFromCardView);

            this.LENGTH = visitNamesArrayButtonFromCardView.length;
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
            tempString += visitNamesArrayButtonFromCardView[position % visitNamesArrayButtonFromCardView.length];
            tempString += "  -  " + visitDatesArrayButtonFromCardView[position % visitDatesArrayButtonFromCardView.length];
            holder.button.setText(tempString);
            // nadawane jest takie samo ID dla przycisku wyboru jak i usuwania wizyty
            holder.button.setTag(queryResultIdArrayList.get(position));
            holder.deleteButton.setTag(queryResultIdArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
