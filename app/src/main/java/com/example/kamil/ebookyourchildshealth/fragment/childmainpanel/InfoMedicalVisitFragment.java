package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Visit;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InfoMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;
    private Unbinder unbinder;
    private String[] textViewNamesArray;
    private int idMedicalVisit;
    private Visit visitObject;
    private static ArrayList<String> queryResultArrayList;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view2, container, false);
        myDebugger = new MyDebugger();
        queryResultArrayList = new ArrayList<>();
        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity()); // activity czy context???

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
//        getChildNameFromIntent();
        getChildDataFromDatabase();
        //setToolbarImageView();
        createAndSetContentAdapter();

        return recyclerView;
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
        }
    }

    private void createAndSetContentAdapter() {
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Adapter to display recycler view.
     *
     * Handle the data collection and bind it to the view.
     *
     * The adapter is a component that stands between the data model
     * we want to show in our app UI and the UI component that renders this information.
     * In other words, an adapter guides the way the information are shown in the UI.
     *
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

        // Set numbers of List in RecyclerView.
        private int LENGTH = 0;

        private final String[] arrayCardViewItemLeftColumnTextViews;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView columnNameTextView;
            public TextView columnValueTextView;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.child_main_panel_fragment_card_item, parent, false));
                columnNameTextView = (TextView) itemView.findViewById(R.id.columnName);
                columnValueTextView = (TextView) itemView.findViewById(R.id.columnValue);
            }
        }

        public ContentAdapter(Context context) {

            Resources resources = context.getResources();

            arrayCardViewItemLeftColumnTextViews = resources.getStringArray(R.array.visit_table);

            this.LENGTH = arrayCardViewItemLeftColumnTextViews.length;
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
            holder.columnNameTextView.setText
                    (arrayCardViewItemLeftColumnTextViews
                            [position % arrayCardViewItemLeftColumnTextViews.length]);

            holder.columnValueTextView.setText
                    (queryResultArrayList.get((position % queryResultArrayList.size())));

            if (position>3)
                holder.columnValueTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));

//            holder.columnValueTextView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 2f));
//            TextView tv = new TextView(v.getContext());
//            tv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
//            holder.columnValueTextView.setText(arrayCardViewItemRightColumnTextViews[position % arrayCardViewItemRightColumnTextViews.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}