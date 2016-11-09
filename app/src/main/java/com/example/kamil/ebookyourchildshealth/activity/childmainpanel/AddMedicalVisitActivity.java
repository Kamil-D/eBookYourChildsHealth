package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddMedicalVisitFragment;

import butterknife.ButterKnife;

public class AddMedicalVisitActivity extends MyActivityOnlyMenuImplemented {

    private Toolbar toolbar;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_visit);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        setToolbars();
        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_medical_visit);
        toolbar.setTitle("Complete all fields");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {
        AddMedicalVisitFragment addMedicalVisitFragment = new AddMedicalVisitFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddMedicalVisit,addMedicalVisitFragment,"fragment");
        fragmentTransaction.commit();
    }

//    @Override
//    public void onBackPressed(){
//        FragmentManager fm = getSupportFragmentManager();
//        if (fm.getBackStackEntryCount() > 0) {
//            fm.popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
