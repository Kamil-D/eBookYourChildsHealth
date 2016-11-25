package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddDiseaseFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddMedicalVisitFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoDiseaseFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoMedicalVisitFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoObjectActivity extends MyActivityOnlyMenuImplemented {

    @BindView(R.id.toolbar_object_info)
    Toolbar toolbar;

    @BindString(R.string.visit_info)
    String toolbarTitle;

    @BindString(R.string.fragment_decision_disease)
    String fragmentDecisionDisease;

    @BindString(R.string.fragment_decision_visit)
    String fragmentDecisionVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_info);

        ButterKnife.bind(this);

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
            InfoMedicalVisitFragment infoMedicalVisitFragment = new InfoMedicalVisitFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewObjectInfo,infoMedicalVisitFragment,"fragment");
            fragmentTransaction.commit();
        }
        else if (fragmentDecision.equals(fragmentDecisionDisease)) {
            InfoDiseaseFragment infoDiseaseFragment = new InfoDiseaseFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewObjectInfo,infoDiseaseFragment,"fragment");
            fragmentTransaction.commit();
        }

//        InfoMedicalVisitFragment infoMedicalVisitFragment = new InfoMedicalVisitFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewInfoMedicalVisit, infoMedicalVisitFragment,"fragment");
//        fragmentTransaction.commit();
    }
}
