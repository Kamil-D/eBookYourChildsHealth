package com.example.kamil.ebookyourchildshealth.activity.childmainpanel;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kamil.ebookyourchildshealth.MyDebugger;
import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.ChildMainPanelFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.DiseasesFragment;
import com.example.kamil.ebookyourchildshealth.fragment.childmainpanel.MedicalVisitsFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildMainPanelActivity extends MyActivityOnlyMenuImplemented
        implements NavigationView.OnNavigationItemSelectedListener {

    MyDebugger myDebugger;
    private final int REQUEST_CODE = 1;
    private String childNameFromIntent;
    private int childIDFromIntent;
    private String childUriFromIntent;
    private Drawable toolbarImageView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;
    private Fragment fragment;
    private String fragmentTypeName = "";

    @BindView(R.id.drawer_child_main_panel)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbarImageChildPanel)
    ImageView imageView;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar_child_panel)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindString(R.string.fragment_decision_disease)
    String fragmentDecisionDisease;

    @BindString(R.string.fragment_decision_visit)
    String fragmentDecisionVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_main_panel);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ButterKnife.bind(this);
        myDebugger = new MyDebugger();

        getBundleFromIntent();
        uriToDrawableArray();
        setToolbars();
        setToolbarImageView();
        setDrawerLayoutAndNavigationView();
        startFragmentTransactionAddNewFragment();
    }


    private void getBundleFromIntent() {
        bundle = getIntent().getBundleExtra("bundle");
        childIDFromIntent = bundle.getInt("childIDFromIntent");
        childNameFromIntent = bundle.getString("childNameFromIntent");
        childUriFromIntent = bundle.getString("childUriFromIntent");
    }

    private void setToolbars() {
        // Set title of page
        collapsingToolbar.setTitle(childNameFromIntent);

        setToolbarName(childNameFromIntent);
        setSupportActionBar(toolbar);
    }

    private void setToolbarName(String name) {
        toolbar.setTitle(name);
    }

    private void setToolbarImageView() {
        //int imageResourceID = getResources().getIdentifier(childNameFromIntent , "drawable", getPackageName());

        imageView.setImageDrawable(toolbarImageView);
//        imageView.setImageDrawable(getResources().getDrawable(R.drawable.elvispresley, getApplicationContext().getTheme()));
//        imageView.setImageDrawable(getResources().getDrawable(imageResourceID, getApplicationContext().getTheme()));
    }

    private void uriToDrawableArray() {
        Uri imageUri = Uri.parse(childUriFromIntent);
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(imageUri);
            toolbarImageView = Drawable.createFromStream(inputStream, imageUri.toString() );
        } catch (FileNotFoundException e) {
            toolbarImageView = getResources().getDrawable(R.drawable.elvispresley);
        }
    }

    private void setDrawerLayoutAndNavigationView() {
        // // Adding menu icon (called hamburger icon) to Toolbar

        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, (DrawerLayout) findViewById(R.id.drawer_child_main_panel), R.string.app_name , R.string.app_name);
        //actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
//        setupDrawerContent(navigationView);
    }

    private void startFragmentTransactionAddNewFragment() {
        ChildMainPanelFragment fragment = new ChildMainPanelFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearLayoutInNestedScrollViewChildPanel, fragment, "fragment");
        fragmentTransaction.commit();

    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        int id = item.getItemId();

        fragment = null;

        if (id == R.id.navigation_item_home_page) {
            NavUtils.navigateUpFromSameTask(this);

        } else if (id == R.id.navigation_item_child_panel) {
            newActivityGoToChildPanelActivity();

        } else if (id == R.id.navigation_item_medical_visits) {
            fragment = new MedicalVisitsFragment();
            fragmentTypeName = fragmentDecisionVisit;

        } else if (id == R.id.navigation_item_diseases_history) {
            fragment = new DiseasesFragment();
            fragmentTypeName = fragmentDecisionDisease;
        }


        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.linearLayoutInNestedScrollViewChildPanel, fragment, fragmentTypeName)
                    .commit();

            navigationView.setCheckedItem(id);
            setToolbarName(item.getTitle().toString());
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public void newActivityGoToChildPanelActivity() {
        Intent intent = new Intent(this, ChildMainPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt("childIDFromIntent", childIDFromIntent);
        bundle.putString("childNameFromIntent", childNameFromIntent);
        bundle.putString("childUriFromIntent", childUriFromIntent);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void newActivityGoToInfoPanelActivity(View view) {
        int idObjectToShow = getButtonVisitTag(view);
        MedicalVisitsFragment medicalVisitsFragment;
        DiseasesFragment diseasesFragment;

        Intent intent = new Intent(this, InfoObjectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("childIDFromIntent", childIDFromIntent);
        bundle.putInt("idObjectToShow", idObjectToShow);
        bundle.putString("childNameFromIntent", childNameFromIntent);

        if (fragmentTypeName ==fragmentDecisionVisit) {
            medicalVisitsFragment = (MedicalVisitsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTypeName);

            bundle.putString("fragmentDecision",fragmentDecisionVisit);
            intent.putExtra("bundle", bundle);

            if (medicalVisitsFragment != null && medicalVisitsFragment.isVisible()) {
                medicalVisitsFragment.newActivityGoToInfoMedicalVisitActivity(intent);
            }
        }
        else if (fragmentTypeName ==fragmentDecisionDisease) {
            diseasesFragment = (DiseasesFragment) getSupportFragmentManager().findFragmentByTag(fragmentTypeName);

            bundle.putString("fragmentDecision",fragmentDecisionDisease);
            intent.putExtra("bundle", bundle);

            if (diseasesFragment != null && diseasesFragment.isVisible()) {
                diseasesFragment.newActivityGoToInfoDiseaseActivity(intent);
            }
        }

//        MedicalVisitsFragment myFragment = (MedicalVisitsFragment)getSupportFragmentManager().findFragmentByTag(fragmentDecisionVisit);
//        if (myFragment != null && myFragment.isVisible()) {
//            myFragment.newActivityGoToInfoMedicalVisitActivity(intent);
//        }
    }

    public void deleteRecordFromDB(View view) {
        int idObjectToDelete = getImageButtonDeleteTag(view);
        MedicalVisitsFragment medicalVisitsFragment;
        DiseasesFragment diseasesFragment;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("idObjectToDelete", idObjectToDelete);
        intent.putExtra("bundle", bundle);

        if (fragmentTypeName ==fragmentDecisionVisit) {
            medicalVisitsFragment = (MedicalVisitsFragment) getSupportFragmentManager().findFragmentByTag(fragmentTypeName);

            if (medicalVisitsFragment != null && medicalVisitsFragment.isVisible()) {
                medicalVisitsFragment.deleteMedicalVisit(intent);
            }
        }
        else if (fragmentTypeName ==fragmentDecisionDisease) {
            diseasesFragment = (DiseasesFragment) getSupportFragmentManager().findFragmentByTag(fragmentTypeName);

            if (diseasesFragment != null && diseasesFragment.isVisible()) {
                diseasesFragment.deleteDisease(intent);
            }
        }

    }

    private int getButtonVisitTag(View v) {
        Button button = (Button) v;
        int buttonTag = Integer.parseInt(button.getTag().toString());
        return buttonTag;
    }

    private int getImageButtonDeleteTag(View v) {
        ImageButton button = (ImageButton) v;
        int buttonTag = Integer.parseInt(button.getTag().toString());
        return buttonTag;
    }
}