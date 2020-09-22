package com.unique.simplealarmclock.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.unique.simplealarmclock.model.Alarm;
import com.unique.simplealarmclock.R;
import com.unique.simplealarmclock.service.AlarmService;
import com.unique.simplealarmclock.service.RescheduleAlarmsService;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    Alarm alarm;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {
            Bundle bundle=intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));
            if (bundle!=null)
                alarm =(Alarm)bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if(alarm!=null) {
                if (!alarm.isRecurring()) {
                    startAlarmService(context, alarm);
                } else {
                    if (isAlarmToday(alarm)) {
                        startAlarmService(context, alarm);
                    }
                }
            }
        }
    }

    private boolean isAlarmToday(Alarm alarm1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (alarm1.isMonday())
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (alarm1.isTuesday())
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (alarm1.isWednesday())
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (alarm1.isThursday())
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (alarm1.isFriday())
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (alarm1.isSaturday())
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (alarm1.isSunday())
                    return true;
                return false;
        }
        return false;
    }

    private void startAlarmService(Context context, Alarm alarm1) {
        Intent intentService = new Intent(context, AlarmService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj),alarm1);
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj),bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
