package com.unique.simplealarmclock.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.unique.simplealarmclock.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("SELECT * FROM alarm_table ORDER BY alarmId ASC")
    LiveData<List<Alarm>> getAlarms();

    @Update
    void update(Alarm alarm);

    @Query("Delete from alarm_table where alarmId = :alarmID")
    void delete(int alarmID);
}
