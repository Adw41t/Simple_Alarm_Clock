package com.unique.simplealarmclock.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unique.simplealarmclock.databinding.FragmentAlarmsListBinding;
import com.unique.simplealarmclock.viewmodel.AlarmListViewModel;
import com.unique.simplealarmclock.util.OnToggleAlarmListener;
import com.unique.simplealarmclock.R;
import com.unique.simplealarmclock.adapter.AlarmRecyclerViewAdapter;
import com.unique.simplealarmclock.model.Alarm;

import java.util.List;

public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmListViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private FloatingActionButton addAlarm;
    private FragmentAlarmsListBinding fragmentAlarmsListBinding;
    public AlarmsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAlarmsListBinding = FragmentAlarmsListBinding.inflate(inflater,container,false);
        View view = fragmentAlarmsListBinding.getRoot();

        alarmsRecyclerView = fragmentAlarmsListBinding.fragmentListalarmsRecylerView;
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);

        addAlarm = fragmentAlarmsListBinding.fragmentListalarmsAddAlarm;
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });

        return view;
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(getContext());
            alarmsListViewModel.update(alarm);
        }
    }

    @Override
    public void onDelete(Alarm alarm) {
        if (alarm.isStarted())
            alarm.cancelAlarm(getContext());
        alarmsListViewModel.delete(alarm.getAlarmId());
    }

    @Override
    public void onItemClick(Alarm alarm,View view) {
        if (alarm.isStarted())
            alarm.cancelAlarm(getContext());
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.arg_alarm_obj),alarm);
        Navigation.findNavController(view).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment,args);
    }
}