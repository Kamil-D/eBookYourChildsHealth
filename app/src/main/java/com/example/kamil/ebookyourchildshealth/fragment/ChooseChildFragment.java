package com.example.kamil.ebookyourchildshealth.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.kamil.ebookyourchildshealth.activity.addnewchild.AddNewChildActivity;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.ChildMainPanelActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseChildFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private static ArrayList<String> queryResultNamesArrayList;
    private static ArrayList<Integer> queryResultIdArrayList;
    private static ArrayList<String> queryResultUriImagesArrayList;
    private static Drawable[] drawableArrayFromUriImagesArrayList;
    private Intent intent;
    static MyDebugger myDebugger;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_child, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_choose_child);
        ButterKnife.bind(this, view);
        queryResultNamesArrayList = new ArrayList<>();
        queryResultIdArrayList = new ArrayList<>();
        queryResultUriImagesArrayList = new ArrayList<>();
        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());

        getChildNamesAndImagesFromDatabase();
        uriToDrawableArray();
        createAndSetContentAdapter();

        return view;
    }

    public void getChildNamesAndImagesFromDatabase() {
        Cursor cursor = myDatabaseHelper.readAllChildIdNamesImages();
        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            queryResultIdArrayList.add(cursor.getInt(0));
            queryResultNamesArrayList.add(cursor.getString(1));
            queryResultUriImagesArrayList.add(cursor.getString(2));
        }
        // dopiero tutaj tworzymy tablicę Drawable bo wiemy już ile elementów ma tablica uri (lub id, name)
        drawableArrayFromUriImagesArrayList = new Drawable[queryResultUriImagesArrayList.size()];
    }

    private void uriToDrawableArray() {
        for (int i=0 ; i<queryResultUriImagesArrayList.size() ; i++) {

            Uri imageUri = Uri.parse(queryResultUriImagesArrayList.get(i));
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                drawableArrayFromUriImagesArrayList[i] = Drawable.createFromStream(inputStream, imageUri.toString() );
            } catch (FileNotFoundException e) {
                drawableArrayFromUriImagesArrayList[i] = getResources().getDrawable(R.drawable.elvispresley);
            }
        }
    }

    private void createAndSetContentAdapter() {
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @OnClick(R.id.buttonAddChild)
    public void newActivityAddNewChildActivity() {
        intent = new Intent(this.getActivity(),AddNewChildActivity.class);
        startActivity(intent);
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

        private String[] namesArrayCardViewItem = new String[queryResultNamesArrayList.size()];
        private Integer[] idArrayCardViewItem = new Integer[queryResultIdArrayList.size()];
        private String[] uriImagesArrayCard = new String[queryResultUriImagesArrayList.size()];

        private final Drawable[] imagesDrawableArray;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ImageButton pictureImageButton;
            public TextView childNameTextViewBottomImageButton;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.choose_child_fragment_card_item, parent, false));
                pictureImageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
                childNameTextViewBottomImageButton = (TextView) itemView.findViewById(R.id.childName);
            }

            public int getPos(View view) {

                int position = view.getId();
                return position;
            }
        }

        public ContentAdapter(Context context) {
            // konwertowanie ArrayList na Array
            namesArrayCardViewItem = queryResultNamesArrayList.toArray(namesArrayCardViewItem);
            idArrayCardViewItem = queryResultIdArrayList.toArray(idArrayCardViewItem);
            uriImagesArrayCard = queryResultUriImagesArrayList.toArray(uriImagesArrayCard);

            imagesDrawableArray = drawableArrayFromUriImagesArrayList;

            LENGTH = namesArrayCardViewItem.length;
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
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(uriImagesArrayCard[position % uriImagesArrayCard.length], holder.pictureImageButton);

            //holder.pictureImageButton.setImageDrawable(imagesDrawableArray[position % imagesDrawableArray.length]);
            holder.childNameTextViewBottomImageButton.setText(namesArrayCardViewItem[position % namesArrayCardViewItem.length]);
            /**
             * ImageButton ma ustawiony tag o nazwie imienia dziecka, aby można było później
             * po kliknięciu na niego, wysłać imię do aktywności odpowiadającej za główny panel dziecka.
             * Na podstawie wysłanego imienia szukany jest odpowiedni obrazek.
             * Gdy zostanie podpięta baza danych będzie się to odbywać prawdopodobnie po jakimś ID
             */
            String tagString = String.valueOf(holder.childNameTextViewBottomImageButton.getText());
            holder.pictureImageButton.setTag(R.integer.tagImageButtonOne, tagString);
            holder.pictureImageButton.setTag(R.integer.tagImageButtonTwo, idArrayCardViewItem[position % idArrayCardViewItem.length]);
            holder.pictureImageButton.setTag(R.integer.tagImageButtonThree, uriImagesArrayCard[position % uriImagesArrayCard.length]);

//            holder.pictureImageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    newActivity();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
