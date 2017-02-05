package com.example.kamil.ebookyourchildshealth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddDiseaseFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.AddMedicalVisitFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoDiseaseFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoDiseaseTabTwoFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.InfoMedicalVisitFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoObjectActivity extends MyActivityOnlyMenuImplemented {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private InfoMedicalVisitFragment infoMedicalVisitFragment;
    private InfoDiseaseFragment infoDiseaseFragment;
    MyDebugger myDebugger;

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
        myDebugger = new MyDebugger();
        ButterKnife.bind(this);

        setToolbars();

        if (savedInstanceState == null) {
            startFragmentTransactionAddNewFragment();
        } else if (fragmentDecisionVisit == getFragmentDecisionFromIntent()){
            infoMedicalVisitFragment = (InfoMedicalVisitFragment) getSupportFragmentManager()
                    .findFragmentByTag(fragmentDecisionVisit);

        } else if (fragmentDecisionDisease == getFragmentDecisionFromIntent()) {
            infoDiseaseFragment = (InfoDiseaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(fragmentDecisionDisease);
        }

    }

    private void setToolbars() {
        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {

        String fragmentDecision = getFragmentDecisionFromIntent();

        if (fragmentDecision.equals(fragmentDecisionVisit)) {
            infoMedicalVisitFragment = new InfoMedicalVisitFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewObjectInfo,
                    infoMedicalVisitFragment, fragmentDecisionVisit);

            fragmentTransaction.commit();
        }
        else if (fragmentDecision.equals(fragmentDecisionDisease)) {
            infoDiseaseFragment = new InfoDiseaseFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewObjectInfo,
                    infoDiseaseFragment, fragmentDecisionDisease);

            fragmentTransaction.commit();
        }
    }

    private String getFragmentDecisionFromIntent() {
        String fragmentDecision = getIntent().getBundleExtra("bundle").getString("fragmentDecision");
        return fragmentDecision;
    }

    public void deleteNoteFromDB(View view) {
        int idObjectToDelete = getImageButtonDeleteTag(view);
        InfoDiseaseFragment infoDiseaseFragment;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("idObjectToDelete", idObjectToDelete);
        intent.putExtra("bundle", bundle);

        infoDiseaseFragment = (InfoDiseaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentDecisionDisease);
        myDebugger.someMethod("InfoObjectActivity = " + infoDiseaseFragment);
        infoDiseaseFragment.deleteNote(intent);
    }

    public void deleteRecordFromDB(View view) {
        int idObjectToDelete = getImageButtonDeleteTag(view);
        InfoMedicalVisitFragment infoMedicalVisitFragment;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("idObjectToDelete", idObjectToDelete);
        intent.putExtra("bundle", bundle);

        infoMedicalVisitFragment = (InfoMedicalVisitFragment) getSupportFragmentManager().findFragmentByTag(fragmentDecisionVisit);
        infoMedicalVisitFragment.deleteReminder(intent);
    }

    private int getImageButtonDeleteTag(View v) {
        ImageButton button = (ImageButton) v;
        int buttonTag = Integer.parseInt(button.getTag().toString());
        return buttonTag;
    }
}