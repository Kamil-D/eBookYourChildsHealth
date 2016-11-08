package com.example.kamil.ebookyourchildshealth.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Unbinder;


public class ChooseChildFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private static ArrayList<String> queryResultNamesArrayList;
    private static ArrayList<Integer> queryResultIdArrayList;
    private Unbinder unbinder;
    static MyDebugger myDebugger;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        queryResultNamesArrayList = new ArrayList<>();
        queryResultIdArrayList = new ArrayList<>();

        myDebugger = new MyDebugger();
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view2, container, false);

        myDatabaseHelper = MyDatabaseHelper.getInstance(getActivity()); // activity czy context???
        getChildNamesAndImagesFromDatabase();

        createAndSetContentAdapter();

        return recyclerView;
    }

    public void getChildNamesAndImagesFromDatabase() {
        Cursor cursor = myDatabaseHelper.readAllChildIdNamesImages();
        if(cursor.getCount() == 0) {
            return;
        }

        StringBuffer stringBuffer = new StringBuffer();
        while(cursor.moveToNext()) {
            stringBuffer.append("ID :" + cursor.getString(0) + "\n");
            queryResultIdArrayList.add(cursor.getInt(0));
            queryResultNamesArrayList.add(cursor.getString(1));
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
        int tagOne = 0;
        int tagTwo = 0;

        private String[] namesArrayCardViewItem = new String[queryResultNamesArrayList.size()];
        private Integer[] idArrayCardViewItem = new Integer[queryResultIdArrayList.size()];

        private final Drawable[] imagesArray;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ImageButton pictureImageButton;
            public TextView childNameTextViewBottomImageButton;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.choose_child_fragment_card_item, parent, false));
                pictureImageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
                childNameTextViewBottomImageButton = (TextView) itemView.findViewById(R.id.childName);
            }
        }

        public ContentAdapter(Context context) {

            Resources resources = context.getResources();

            tagOne = resources.getInteger(R.integer.tagOne);
            tagTwo = resources.getInteger(R.integer.tagTwo);

            // konwertowanie ArrayList na Array
            namesArrayCardViewItem = queryResultNamesArrayList.toArray(namesArrayCardViewItem);
            idArrayCardViewItem = queryResultIdArrayList.toArray(idArrayCardViewItem);

            TypedArray typedArray = resources.obtainTypedArray(R.array.child_images);
            imagesArray = new Drawable[typedArray.length()];

            for (int i = 0; i < imagesArray.length; i++) {
                imagesArray[i] = typedArray.getDrawable(i);
            }
            LENGTH = namesArrayCardViewItem.length;
            typedArray.recycle();
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
            holder.pictureImageButton.setImageDrawable(imagesArray[position % imagesArray.length]);
            holder.childNameTextViewBottomImageButton.setText(namesArrayCardViewItem[position % namesArrayCardViewItem.length]);
//            holder.pictureImageButton.setImageDrawable(imagesArray[position % namesArrayCardViewItem.length]);
//            holder.childNameTextViewBottomImageButton.setText(namesArrayList.get(position % namesArrayList.size()));
//            holder.columnValueTextView.setText(queryResultArrayList.get(counterArrayListIndex));
            /**
             * ImageButton ma ustawiony tag o nazwie imienia dziecka, aby można było później
             * po kliknięciu na niego, wysłać imię do aktywności odpowiadającej za główny panel dziecka.
             * Na podstawie wysłanego imienia szukany jest odpowiedni obrazek.
             * Gdy zostanie podpięta baza danych będzie się to odbywać prawdopodobnie po jakimś ID
             */
            String tagString = String.valueOf(holder.childNameTextViewBottomImageButton.getText());
            holder.pictureImageButton.setTag(R.integer.tagOne, tagString);
            holder.pictureImageButton.setTag(R.integer.tagTwo, idArrayCardViewItem[position % namesArrayCardViewItem.length]);
            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(uri, holder.pictureImageButton);   // URI z bd
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
