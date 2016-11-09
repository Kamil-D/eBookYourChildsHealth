package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoMedicalVisitFragment;

import butterknife.ButterKnife;

public class InfoMedicalVisitActivity extends MyActivityOnlyMenuImplemented {

    private Toolbar toolbar;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_visit_info);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        setToolbars();
        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_medical_visit_info);
        toolbar.setTitle("Visit info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {
        InfoMedicalVisitFragment infoMedicalVisitFragment = new InfoMedicalVisitFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewInfoMedicalVisit, infoMedicalVisitFragment,"fragment");
        fragmentTransaction.commit();
    }
}
