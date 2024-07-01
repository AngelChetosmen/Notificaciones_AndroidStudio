package com.example.notificacionesalarmas_vrja;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String description = intent.getStringExtra("description");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyAlarm")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener un ícono pequeño en tu carpeta drawable
                .setContentTitle("Alarm Notification")
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
