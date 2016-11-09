package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.AddMedicalVisitActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.util.util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MedicalVisitsFragment extends Fragment {

    private final int REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDatabaseHelper;
    private Unbinder unbinder;
    private Intent intent;
    private int childIDFromIntent;
    private String childNameFromIntent;
    private static ArrayList<Integer> queryResultIdArrayList;
    private static ArrayList<String> queryResultNamesArrayList;
    private static ArrayList<String> queryResultDatesArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_visits, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_medical_visits);
        unbinder = ButterKnife.bind(this, view);
        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity()); // activity czy context???

        queryResultIdArrayList = new ArrayList<>();
        queryResultNamesArrayList = new ArrayList<>();
        queryResultDatesArrayList = new ArrayList<>();

        getBundleFromIntent();
//        getChildIDFromIntent();
//        getChildNameFromIntent();
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
//        intent.putExtra("childNameFromIntent", childNameFromIntent);
//        intent.putExtra("childIDFromIntent", childIDFromIntent);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("chuj", "on act result"+resultCode+" " + requestCode);
        if (resultCode== util.RESULT_CODE) {
            getVisitDataFromDatabase();
            createAndSetContentAdapter();
            Log.d("chuj", "dziala");
        }
    }

    private void getBundleFromIntent() {
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
        childNameFromIntent = bundle.getString("childNameFromIntent");
    }

    private void getChildNameFromIntent() {
        childNameFromIntent = getActivity().getIntent().getStringExtra("childNameFromIntent");
    }

    private void getChildIDFromIntent() {
        int defaultValue = 0;
        childIDFromIntent = getActivity().getIntent().getIntExtra("childIDFromIntent", defaultValue);
    }

    public void getVisitDataFromDatabase() {
        queryResultIdArrayList = new ArrayList<>();
        queryResultNamesArrayList = new ArrayList<>();
        queryResultDatesArrayList = new ArrayList<>();

        Cursor cursor = myDatabaseHelper.readChildMedicalVisitsData(childIDFromIntent);

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

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.medical_visits_fragment_card_item, parent, false));
                button = (Button) itemView.findViewById(R.id.buttonMedicalVisitsList);
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
            holder.button.setTag(queryResultIdArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
