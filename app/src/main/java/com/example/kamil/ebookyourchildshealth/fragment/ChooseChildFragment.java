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
import com.example.kamil.ebookyourchildshealth.activity.AddObjectActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Child;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseChildFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private static ArrayList<Child> childRecyclerViewItemArrayList;
    private static Drawable[] drawableArrayFromUri;
    private Intent intent;
    static MyDebugger myDebugger;
    private RecyclerView recyclerView;

    @BindString(R.string.fragment_decision_child)
    String fragmentDecisionChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_child, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_choose_child);
        ButterKnife.bind(this, view);

        childRecyclerViewItemArrayList = new ArrayList<>();
        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(getActivity());

        getChildNamesAndImagesFromDatabase();
        uriToDrawableArray();
        createAndSetContentAdapter();

        return view;
    }

    public void getChildNamesAndImagesFromDatabase() {
        Cursor cursor = myDatabaseHelper.readAllChildsIdNamesImages();
        Child child;

        if(cursor.getCount() == 0) {
            return;
        }

        while(cursor.moveToNext()) {
            child = new Child(cursor.getInt(0), cursor.getString(1), Uri.parse(cursor.getString(2)));
            childRecyclerViewItemArrayList.add(child);
        }
        // dopiero tutaj tworzymy tablicę Drawable bo wiemy już ile elementów ma tablica uri (lub id, name)
        drawableArrayFromUri = new Drawable[childRecyclerViewItemArrayList.size()];
    }

    private void uriToDrawableArray() {
        for (int i = 0; i< childRecyclerViewItemArrayList.size() ; i++) {

//            Uri imageUri = Uri.parse(childRecyclerViewItemArrayList.get(i).getImageUri());
            Uri imageUri = childRecyclerViewItemArrayList.get(i).getImageUri();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                drawableArrayFromUri[i] = Drawable.createFromStream(inputStream, imageUri.toString() );
            } catch (FileNotFoundException e) {
                drawableArrayFromUri[i] = getResources().getDrawable(R.drawable.elvispresley);
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
        intent = new Intent(this.getActivity(),AddObjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fragmentDecision", fragmentDecisionChild);
        intent.putExtra("bundle", bundle);
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

        private ArrayList<Child> childCardViewItem = new ArrayList<>();

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ImageButton pictureImageButton;
            public TextView childNameTextViewBelowImageButton;

            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

                super(inflater.inflate(R.layout.choose_child_fragment_card_item, parent, false));
                pictureImageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
                childNameTextViewBelowImageButton = (TextView) itemView.findViewById(R.id.childName);
            }
        }

        public ContentAdapter(Context context) {

            childCardViewItem = childRecyclerViewItemArrayList;

            LENGTH = childCardViewItem.size();
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
            imageLoader.displayImage(childCardViewItem.get(position).getImageUri().toString(), holder.pictureImageButton);

            holder.childNameTextViewBelowImageButton.setText(childCardViewItem.get(position).getName());
            /**
             * ImageButton ma ustawiony tag o nazwie imienia dziecka, aby można było później
             * po kliknięciu na niego, wysłać imię do aktywności odpowiadającej za główny panel dziecka.
             * Na podstawie wysłanego imienia szukany jest odpowiedni obrazek.
             * Gdy zostanie podpięta baza danych będzie się to odbywać prawdopodobnie po jakimś ID
             */
            String tagString = String.valueOf(holder.childNameTextViewBelowImageButton.getText());
            holder.pictureImageButton.setTag(R.integer.tagImageButtonOne, tagString);
            holder.pictureImageButton.setTag(R.integer.tagImageButtonTwo, childCardViewItem.get(position).getId());
            holder.pictureImageButton.setTag(R.integer.tagImageButtonThree, childCardViewItem.get(position).getImageUri().toString());

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}
