package com.math4.user.mathplace;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static com.math4.user.mathplace.Settings.timeNotify;
import static com.math4.user.mathplace.Zactavka.userName;

public class TimeNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils mNotificationUtils = new NotificationUtils(context);
        Notification.Builder nb;
        if(userName!=null) {
            nb = mNotificationUtils.
                    getAndroidChannelNotification("Привет, " + userName, "Пора заняться математикой", context);
        }else{
            nb = mNotificationUtils.
                    getAndroidChannelNotification("Привет", "Пора заняться математикой", context);
        }
        mNotificationUtils.getManager().notify(101, nb.build());
// Установим следующее напоминание.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}