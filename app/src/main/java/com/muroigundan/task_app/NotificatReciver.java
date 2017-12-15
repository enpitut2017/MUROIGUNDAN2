package com.muroigundan.task_app; /**
 * Created by knight on 2017/11/10.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

public class NotificatReciver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent receivedIntent) {

        int notificationId = receivedIntent.getIntExtra("notificationId", 0);
        NotificationManager myNotification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent bootIntent =
                new Intent(context, MainActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, bootIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("通知")
                .setContentText(receivedIntent.getCharSequenceExtra("todo"))
                .setWhen(System.currentTimeMillis())
                .setPriority(PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        myNotification.notify(notificationId, builder.build());
        Toast.makeText(context, "NOTICE", Toast.LENGTH_SHORT).show();
    }
}
