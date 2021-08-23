package com.example.user.todooo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.View;

/**
 * Created by user on 2017-12-07.
 */

public class AlarmReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            NotificationManager notificationmanager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, new Intent(arg0, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(arg0);
            builder.setSmallIcon(R.drawable.moon).setTicker("HETT").setWhen(System.currentTimeMillis())
                    .setNumber(1).setContentTitle("TODAY").setContentText("오늘 끝내야 할 일은 없나요?")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
            notificationmanager.notify(1, builder.build());

        }

    }
