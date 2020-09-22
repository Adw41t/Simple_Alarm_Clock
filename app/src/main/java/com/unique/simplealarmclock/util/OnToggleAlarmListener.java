package com.unique.simplealarmclock.util;

import android.view.View;

import com.unique.simplealarmclock.model.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onDelete(Alarm alarm);
    void onItemClick(Alarm alarm,View view);
}
