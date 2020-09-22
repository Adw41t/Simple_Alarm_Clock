package com.unique.simplealarmclock.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.unique.simplealarmclock.model.Alarm;
import com.unique.simplealarmclock.R;
import com.unique.simplealarmclock.activities.RingActivity;

import java.io.IOException;

import static com.unique.simplealarmclock.App.CHANNEL_ID;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    Alarm alarm;
    Uri ringtone;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ringtone= RingtoneManager.getActualDefaultRingtoneUri(this.getBaseContext(), RingtoneManager.TYPE_ALARM);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle!=null)
            alarm =(Alarm)bundle.getSerializable(getString(R.string.arg_alarm_obj));
        Intent notificationIntent = new Intent(this, RingActivity.class);
        notificationIntent.putExtra(getString(R.string.bundle_alarm_obj),bundle);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String alarmTitle=getString(R.string.alarm_title);
        if(alarm!=null) {
            alarmTitle = alarm.getTitle();
            try {
                mediaPlayer.setDataSource(this.getBaseContext(), Uri.parse(alarm.getTone()));
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else{

            try {
                mediaPlayer.setDataSource(this.getBaseContext(),ringtone);
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ring Ring .. Ring Ring")
                .setContentText(alarmTitle)
                .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent,true)
                .build();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        if(alarm.isVibrate()) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
