package com.example.kamil.ebookyourchildshealth.activity.addnewchild;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.AddNewChildFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewChildActivity extends MyActivityOnlyMenuImplemented {

    private Toolbar toolbar;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        setToolbars();

        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_child);
        toolbar.setTitle("Complete all fields");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {
        AddNewChildFragment addNewChildFragment = new AddNewChildFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddChild,addNewChildFragment,"fragment");
        fragmentTransaction.commit();
    }

}
