package com.david.coursework;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Reminder extends AppCompatActivity implements  View.OnClickListener{
    private int notificationId = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);
        getSupportActionBar().hide();
        findViewById(R.id.buttonSetReminder).setOnClickListener(this);
        findViewById(R.id.buttonCancelReminder).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TimePicker timePicker = findViewById(R.id.timePicker);

        // Set notificationId and Text
        Intent intent = new Intent(Reminder.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("to do", "Hey, it's Journal Time!");

        //getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Reminder.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (v.getId()) {
            case R.id.buttonSetReminder:
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Create time
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                // Set alarm
                //set(type, milliseconds, intent)
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime, AlarmManager.INTERVAL_DAY, alarmIntent);

                Toast.makeText(this, "Notification set!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonCancelReminder:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
