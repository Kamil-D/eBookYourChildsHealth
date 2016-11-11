package com.example.kamil.ebookyourchildshealth.activity.addnewchild;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kamil.ebookyourchildshealth.R;
import com.example.kamil.ebookyourchildshealth.activity.MyActivityOnlyMenuImplemented;
import com.example.kamil.ebookyourchildshealth.database.MyDatabaseHelper;
import com.example.kamil.ebookyourchildshealth.fragment.AddNewChildFragment;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewChildActivity extends MyActivityOnlyMenuImplemented {

    @BindView(R.id.toolbar_add_child)
    Toolbar toolbar;
    private Intent intent;
    private MyDatabaseHelper myDatabaseHelper;
    AddNewChildFragment addNewChildFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_child);

        ButterKnife.bind(this);
        myDatabaseHelper = MyDatabaseHelper.getInstance(this);

        setToolbars();

        startFragmentTransactionAddNewFragment();
    }

    private void setToolbars() {
        toolbar.setTitle("Complete all fields");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void startFragmentTransactionAddNewFragment() {
        addNewChildFragment = new AddNewChildFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.firstLinearLayoutInNestedScrollViewAddChild,addNewChildFragment,"fragment");
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("crop", resultUri + " //// " + resultUri.getPath());
//                ImageLoader imageLoader = ImageLoader.getInstance();
//                imageLoader.displayImage("file://" + resultUri.getPath(), imageButton);
                try {
                    addNewChildFragment.setImage("file://" + resultUri.getPath(), resultUri);
//                    croppedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
