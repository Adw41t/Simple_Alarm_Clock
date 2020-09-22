package com.unique.simplealarmclock.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unique.simplealarmclock.R;
import com.unique.simplealarmclock.databinding.ItemAlarmBinding;
import com.unique.simplealarmclock.model.Alarm;
import com.unique.simplealarmclock.util.DayUtil;
import com.unique.simplealarmclock.util.OnToggleAlarmListener;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;
    private Switch alarmStarted;
    private ImageButton deleteAlarm;
    private View itemView;
    private TextView alarmDay;
    public AlarmViewHolder(@NonNull ItemAlarmBinding itemAlarmBinding) {
        super(itemAlarmBinding.getRoot());
        alarmTime = itemAlarmBinding.itemAlarmTime;
        alarmStarted = itemAlarmBinding.itemAlarmStarted;
        alarmRecurring = itemAlarmBinding.itemAlarmRecurring;
        alarmRecurringDays = itemAlarmBinding.itemAlarmRecurringDays;
        alarmTitle = itemAlarmBinding.itemAlarmTitle;
        deleteAlarm= itemAlarmBinding.itemAlarmRecurringDelete;
        alarmDay = itemAlarmBinding.itemAlarmDay;
        this.itemView=itemAlarmBinding.getRoot();
    }

    public void bind(Alarm alarm, OnToggleAlarmListener listener) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("My alarm");
        }
        if(alarm.isRecurring()){
            alarmDay.setText(alarm.getRecurringDaysText());
        }
        else {
            alarmDay.setText(DayUtil.getDay(alarm.getHour(),alarm.getMinute()));
        }
        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isShown() || buttonView.isPressed())
                    listener.onToggle(alarm);
            }
        });

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete(alarm);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(alarm,view);
            }
        });
    }
}