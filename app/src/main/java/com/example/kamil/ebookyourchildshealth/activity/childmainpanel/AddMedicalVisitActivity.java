package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddMedicalVisitFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMedicalVisitActivity extends MyActivityOnlyMenuImplemented {

    @BindView(R.id.toolbar_add_medical_visit)
    Toolbar toolbar;
    @BindString(R.string.complete_all_fields)
    String toolbarTitle;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_visit);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(this);

        setToolbars();
        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar.setTitle(toolbarTitle);
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

}
