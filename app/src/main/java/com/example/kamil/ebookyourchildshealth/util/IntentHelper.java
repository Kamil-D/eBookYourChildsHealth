package com.example.kamil.ebookyourchildshealth.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by KamilosD on 2016-11-08.
 */
public class IntentHelper {
    public final static int FILE_PICK = 11;

    public static void chooseFileIntent(Fragment fragment){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  // nowy intent i ustawia typ na pobranie plików
        intent.setType("image/*");  // typ pliku
        fragment.startActivityForResult(intent, FILE_PICK);
    }
    public static void startActivityIntent(Activity source, Class<? extends Activity> nextActivity){
        Intent  i = new Intent(source,nextActivity);
        source.startActivity(i);
    }
    public static void startActivityIntent(Activity source, Class<? extends Activity> nextActivity, Bundle bundle){
        Intent  i = new Intent(source,nextActivity);
        i.putExtras(bundle);
        source.startActivity(i);
    }
}