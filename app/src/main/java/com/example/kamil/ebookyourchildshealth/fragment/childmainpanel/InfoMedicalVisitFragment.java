package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Visit;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InfoMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;
    private Unbinder unbinder;
    private String[] textViewNamesArray;
    private int childIDfromIntent;
    private Visit visitObject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medical_visit, container, false);
        myDebugger = new MyDebugger();
        unbinder = ButterKnife.bind(this, view);
        myDatabaseHelper = new MyDatabaseHelper(getActivity()); // activity czy context???


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDatabaseHelper != null) {
            myDatabaseHelper = null;
        }
    }
}
