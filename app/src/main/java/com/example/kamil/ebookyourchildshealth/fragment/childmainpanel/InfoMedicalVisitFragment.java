package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Reminder;
import com.example.kamil.ebookyourchildshealth.model.Visit;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoMedicalVisitFragment extends Fragment {

    MyDebugger myDebugger;
    private static final int NUM_ITEMS = 2;
    private static ViewPager mViewPager;
    //    private FragmentPagerAdapter mAdapterViewPager;
    private CustomFragmentPagerAdapter mAdapterViewPager;
    private TabLayout mTabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_medical_visit, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        mAdapterViewPager = new CustomFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        mTabLayout.setVisibility(View.VISIBLE);

        mTabLayout.setupWithViewPager(mViewPager);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(getActivity(), "Przytrzymaj wybrane pole, aby edytować", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 70);
        toast.show();
    }

    public void deleteReminder(Intent intent) {
        int index = 1;

        ((InfoMedicalVisitTabTwoFragment) mAdapterViewPager.getFragment(index)).deleteReminder(intent);
    }

    public static class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private InfoMedicalVisitTabOneFragment infoMedicalVisitTabOneFragment;
        private InfoMedicalVisitTabTwoFragment infoMedicalVisitTabTwoFragment;

        public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    infoMedicalVisitTabOneFragment = new InfoMedicalVisitTabOneFragment();
                    return infoMedicalVisitTabOneFragment;
                case 1:
                    infoMedicalVisitTabTwoFragment = new InfoMedicalVisitTabTwoFragment();
                    return infoMedicalVisitTabTwoFragment;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Przypomnienia";
            }
            return null;
        }

        public Fragment getFragment(int index) {
            switch (index) {
                case 0:
                    return infoMedicalVisitTabOneFragment;
                case 1:
                    return infoMedicalVisitTabTwoFragment;
            }
            return null;
        }
    }

}

















