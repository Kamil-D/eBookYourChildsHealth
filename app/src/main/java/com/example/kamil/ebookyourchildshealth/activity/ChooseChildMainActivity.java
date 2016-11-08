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

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseChildMainActivity extends MyActivityOnlyMenuImplemented {

    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private Intent intent;
    MyDebugger myDebugger;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_child_main);

        ButterKnife.bind(this);

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
        String childNameFromIntent = getImageButtonTagString(view);
        int childIDFromIntent = getImageButtonTagInt(view);
        intent.putExtra("childNameFromIntent", childNameFromIntent);
        intent.putExtra("childIDFromIntent", childIDFromIntent);
        startActivity(intent);
    }

    private String getImageButtonTagString (View v) {
        ImageButton imageButton = (ImageButton) v;
        String imageButtonTagName = (String) imageButton.getTag(R.integer.tagOne);
//        String buttonText = imageButton.getTag().toString();
        return imageButtonTagName;
    }

    private int getImageButtonTagInt (View v) {
        ImageButton imageButton = (ImageButton) v;
        int imageButtonTagInt = (int) imageButton.getTag(R.integer.tagTwo);
        return imageButtonTagInt;
    }

}
