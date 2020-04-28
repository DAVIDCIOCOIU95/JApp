package com.david.coursework;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "exampleChannel";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get id and message from intent
        int notificationId = intent.getIntExtra("notificationId", 0);

        // When notification is tapped, call MainActivity
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Prepare notification
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher_yellow_j)
                .setContentTitle("It's time to Journal!")
        .setWhen(System.currentTimeMillis())
        .setContentIntent(contentIntent)
        .setPriority(Notification.PRIORITY_MAX)
        .setDefaults(Notification.DEFAULT_ALL);

        // Notify
        myNotificationManager.notify(notificationId, builder.build());

    }
}
