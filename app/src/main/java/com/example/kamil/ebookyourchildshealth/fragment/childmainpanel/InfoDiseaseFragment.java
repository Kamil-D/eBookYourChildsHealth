package com.example.kamil.ebookyourchildshealth.fragment.childmainpanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.model.Disease;
import com.example.kamil.ebookyourchildshealth.model.Note;
import com.example.kamil.ebookyourchildshealth.util.UtilCode;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoDiseaseFragment extends Fragment {

    static MyDebugger myDebugger = new MyDebugger();
    private static final int NUM_ITEMS = 2;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapterViewPager;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_disease, container, false);
        ButterKnife.bind(this, view);
        myDebugger = new MyDebugger();

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
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

    public static class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private InfoDiseaseTabOneFragment infoDiseaseTabOneFragment;
        private InfoDiseaseTabTwoFragment infoDiseaseTabTwoFragment;

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
                    return new InfoDiseaseTabOneFragment();
                case 1:
                    return new InfoDiseaseTabTwoFragment();
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
                    return "Notatki";
            }
            return null;
        }
    }



}