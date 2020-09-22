package com.unique.simplealarmclock.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.unique.simplealarmclock.R;
import com.unique.simplealarmclock.databinding.ActivityRingBinding;
import com.unique.simplealarmclock.model.Alarm;
import com.unique.simplealarmclock.service.AlarmService;
import com.unique.simplealarmclock.viewmodel.AlarmListViewModel;

import java.util.Calendar;
import java.util.Random;

public class RingActivity extends AppCompatActivity {
    Alarm alarm;
    private AlarmListViewModel alarmsListViewModel;
    private ActivityRingBinding ringActivityViewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ringActivityViewBinding= ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(ringActivityViewBinding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
/*        KeyguardManager keyguardManager=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE) ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                keyguardManager.requestDismissKeyguard(this, null);
            }*/

        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmListViewModel.class);
        Bundle bundle=getIntent().getBundleExtra(getString(R.string.bundle_alarm_obj));
        if (bundle!=null)
            alarm =(Alarm)bundle.getSerializable(getString(R.string.arg_alarm_obj));

        ringActivityViewBinding.activityRingDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAlarm();
            }
        });

        ringActivityViewBinding.activityRingSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeAlarm();
            }
        });

        animateClock();
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(ringActivityViewBinding.activityRingClock, "rotation", 0f, 30f, 0f, -30f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false);
            setTurnScreenOn(false);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }
    }

    private void dismissAlarm(){
        if(alarm!=null) {
            alarm.setStarted(false);
            alarm.cancelAlarm(getBaseContext());
            alarmsListViewModel.update(alarm);
        }
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }

    private void snoozeAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);

        if(alarm!=null){
            alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setMinute(calendar.get(Calendar.MINUTE));
            alarm.setTitle("Snooze "+alarm.getTitle());
        }
        else {
            alarm = new Alarm(
                    new Random().nextInt(Integer.MAX_VALUE),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    "Snooze",
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    RingtoneManager.getActualDefaultRingtoneUri(getBaseContext(), RingtoneManager.TYPE_ALARM).toString(),
                    false
            );
        }
        alarm.schedule(getApplicationContext());

        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        finish();
    }
}