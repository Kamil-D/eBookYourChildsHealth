package com.example.kamil.ebookyourchildshealth.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.addnewchild.AddNewChildActivity;
import com.example.kamil.ebookyourchildshealth.activity.childmainpanel.ChildMainPanelActivity;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.ChooseChildFragment;
import com.example.kamil.ebookyourchildshealth.util.ImageLoaderHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseChildMainActivity extends MyActivityOnlyMenuImplemented {

    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private Intent intent;
    private MyDebugger myDebugger;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_main);

        ButterKnife.bind(this);

        ImageLoaderHelper.initialize(this);

        myDebugger = new MyDebugger();
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        setToolbars();
        startFragmentTransactionAddNewFragment();


    }

    private void setToolbars() {
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        collapsingToolbar.setTitle("Choose or add a child ");

        toolbar = (Toolbar) findViewById(R.id.toolbar_choose_child);
        toolbar.setTitle("eBook Child's Health");
        setSupportActionBar(toolbar);
    }

    private void startFragmentTransactionAddNewFragment() {
        ChooseChildFragment chooseChildFragment = new ChooseChildFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearLayoutInNestedScrollViewChooseChild,chooseChildFragment,"myFragment");
        fragmentTransaction.commit();
    }

    @OnClick(R.id.buttonAddChild)
    public void newActivityAddNewChildActivity (View view) {
        intent = new Intent(this,AddNewChildActivity.class);
        startActivity(intent);
    }

    public void newActivityGoToChildPanelActivity (View view) {
        intent = new Intent(this,ChildMainPanelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("childIDFromIntent", getImageButtonTagTwo(view));
        bundle.putString("childNameFromIntent", getImageButtonTagOne(view));
        bundle.putString("childUriFromIntent", getImageButtonTagThree(view));
        intent.putExtra("bundle", bundle);
//        intent.putExtra("childNameFromIntent", imageButtonTagString);
//        intent.putExtra("childIDFromIntent", imageButtonTagInt);
        startActivity(intent);
    }

    private String getImageButtonTagOne(View v) {
        ImageButton imageButton = (ImageButton) v;
        String imageButtonTagName = (String) imageButton.getTag(R.integer.tagImageButtonOne);
//        String buttonText = imageButton.getTag().toString();
        return imageButtonTagName;
    }

    private int getImageButtonTagTwo(View v) {
        ImageButton imageButton = (ImageButton) v;
        int imageButtonTagId = (int) imageButton.getTag(R.integer.tagImageButtonTwo);
        return imageButtonTagId;
    }

    private String getImageButtonTagThree(View v) {
        ImageButton imageButton = (ImageButton) v;
        String imageButtonTagUri = (String) imageButton.getTag(R.integer.tagImageButtonThree);
        return imageButtonTagUri;
    }

}
