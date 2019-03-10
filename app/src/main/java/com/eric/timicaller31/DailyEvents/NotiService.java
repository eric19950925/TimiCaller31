package com.eric.timicaller31.DailyEvents;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.eric.timicaller31.R;

import static com.eric.timicaller31.DailyEvents.App.CHANNEL_ID;

public class NotiService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
//        Intent notificationIntent = new Intent(this, NotiService.class);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("TimiCaller 提醒:")
                .setContentText(input)
                .setSmallIcon(R.drawable.icons8_coffee_to_go)
//                .setAutoCancel(true)
                .setOngoing(false)
//                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
