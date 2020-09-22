package com.unique.simplealarmclock;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannnel();

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean dn = sharedPreferences.getBoolean(getString(R.string.dayNightTheme), true);
        if (dn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.app_name)+"Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
