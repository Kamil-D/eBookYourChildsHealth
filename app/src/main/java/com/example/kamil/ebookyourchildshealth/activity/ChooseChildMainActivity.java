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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseChildMainActivity extends MyActivityOnlyMenuImplemented {

    private Intent intent;
    private MyDebugger myDebugger;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar_choose_child)
    Toolbar toolbar;

    @BindString(R.string.choose_or_add_child)
    String toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_main);

        ButterKnife.bind(this);
        ImageLoaderHelper.initialize(this);
        myDebugger = new MyDebugger();

        setToolbars();
        startFragmentTransactionAddNewFragment();


    }

    private void setToolbars() {
        // Set title of Detail page
        collapsingToolbar.setTitle(toolbarTitle);

        setSupportActionBar(toolbar);
    }

    private void startFragmentTransactionAddNewFragment() {
        ChooseChildFragment chooseChildFragment = new ChooseChildFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearLayoutInNestedScrollViewChooseChild,chooseChildFragment,"myFragment");
        fragmentTransaction.commit();
    }

    public void newActivityGoToChildMainPanelActivity(View view) {
        intent = new Intent(this,ChildMainPanelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("childIDFromIntent", getImageButtonTagTwo(view));
        bundle.putString("childNameFromIntent", getImageButtonTagOne(view));
        bundle.putString("childUriFromIntent", getImageButtonTagThree(view));
        intent.putExtra("bundle", bundle);
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
