package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.AddNewChildFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddDiseaseFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddMedicalVisitFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddObjectActivity extends MyActivityOnlyMenuImplemented {

    private MyDatabaseHelper myDatabaseHelper;

    @BindView(R.id.toolbar_add_medical_visit)
    Toolbar toolbar;

    @BindString(R.string.complete_all_fields)
    String toolbarTitle;

    @BindString(R.string.fragment_decision_disease)
    String fragmentDecisionDisease;

    @BindString(R.string.fragment_decision_visit)
    String fragmentDecisionVisit;

    @BindString(R.string.fragment_decision_child)
    String fragmentDecisionChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getMyDatabaseHelperInstance(this);

        setToolbars();
        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {

        String fragmentDecision = getIntent().getBundleExtra("bundle").getString("fragmentDecision");

        if (fragmentDecision.equals(fragmentDecisionVisit)) {
            AddMedicalVisitFragment addMedicalVisitFragment = new AddMedicalVisitFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddObject,addMedicalVisitFragment,"fragment");
            fragmentTransaction.commit();
        }
        else if (fragmentDecision.equals(fragmentDecisionDisease)) {
            AddDiseaseFragment addDiseaseFragment = new AddDiseaseFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddObject,addDiseaseFragment,"fragment");
            fragmentTransaction.commit();
        }
        else if (fragmentDecision.equals(fragmentDecisionChild)) {
            AddNewChildFragment addNewChildFragment = new AddNewChildFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddObject,addNewChildFragment,"fragment");
            fragmentTransaction.commit();
        }

    }

}
