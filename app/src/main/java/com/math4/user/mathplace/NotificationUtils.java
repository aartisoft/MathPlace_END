package com.math4.user.mathplace;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class NotificationUtils extends ContextWrapper {

    NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.math3.user.notify2";
    public static final String IOS_CHANNEL_ID = "com.math3.user.notify";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL2";
    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";

    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getAndroidChannelNotification(String title, String body, Context context) {
        Intent notificationIntent = new Intent(context, Zactavka.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.school))
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public void cancalNotify(){
        mManager.cancelAll();
    }
}