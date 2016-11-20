package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;

import java.util.ArrayList;


public class ChildMainPanelFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private static ArrayList<String> queryResultArrayList;
    private String childNameFromIntent;
    private ImageView imageView;
    private RecyclerView recyclerView;
    MyDebugger myDebugger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view2, container, false);

        myDebugger = new MyDebugger();
        queryResultArrayList = new ArrayList<>();
        imageView = (ImageView) getActivity().findViewById(R.id.toolbarImageChildPanel);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());

        // najpierw odczytujemy ImageButtonTag, czyli imie dziecka
        // a dopiero potem rekord z bazy danych z konkretnym imieniem dziecka
        getBundleFromIntent();
        getChildDataFromDatabase();
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
        childNameFromIntent = bundle.getString("childNameFromIntent");
    }

    public void getChildDataFromDatabase() {
        Cursor cursor = myDatabaseHelper.readChildData(childNameFromIntent);
        if(cursor.getCount() == 0) {
            return;
        }
        while(cursor.moveToNext()) {
            queryResultArrayList.add(cursor.getString(0));
            queryResultArrayList.add(cursor.getString(1));
            queryResultArrayList.add(cursor.getString(2));
            queryResultArrayList.add(cursor.getString(3));
            queryResultArrayList.add(cursor.getString(4));
            queryResultArrayList.add(cursor.getString(5));
            queryResultArrayList.add(cursor.getString(6));
            queryResultArrayList.add(cursor.getString(7));
            queryResultArrayList.add(cursor.getString(8));
            queryResultArrayList.add(cursor.getString(9));
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
        private final String[] arrayCardViewItemToSetRightColumnTextViews;

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

            arrayCardViewItemLeftColumnTextViews = resources.getStringArray(R.array.child_table);
            arrayCardViewItemToSetRightColumnTextViews = resources.getStringArray(R.array.temporary_child_data);

            this.LENGTH = arrayCardViewItemToSetRightColumnTextViews.length;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }


        /**
         * Specify the contents of each item of the RecyclerView.
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.columnNameTextView.setText
                    (arrayCardViewItemLeftColumnTextViews
                            [position % arrayCardViewItemLeftColumnTextViews.length]);

            holder.columnValueTextView.setText
                    (queryResultArrayList.get((position % arrayCardViewItemLeftColumnTextViews.length)+1));
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
