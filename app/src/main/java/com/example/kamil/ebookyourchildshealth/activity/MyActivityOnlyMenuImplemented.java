package com.example.kamil.ebookyourchildshealth.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kamil.ebookyourchildshealth.R;

/**
 * Created by kamil on 2016-10-31.
 */

public class MyActivityOnlyMenuImplemented extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_about) {
            return true;
        }
        else if (id == R.id.action_main_panel) {
            openMainActivity();
            return true;
        }
        else if (id == R.id.action_author) {
            openActivityUrlAddress();
            return true;
        }
        else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openMainActivity() {
        Intent intent = new Intent(this,ChooseChildMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openActivityUrlAddress() {
        Uri url = Uri.parse("https://www.facebook.com/kamilosdzialek");
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(intent);
    }
}