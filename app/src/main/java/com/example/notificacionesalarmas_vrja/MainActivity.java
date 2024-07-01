package com.example.notificacionesalarmas_vrja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText etDescription;
    private Button btnSetAlarm;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        etDescription = findViewById(R.id.etDescription);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        timePicker.setIs24HourView(true); // Establecer el modo de 24 horas en código

        createNotificationChannel();

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
    }

    private void setAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String description = etDescription.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("Descripcion de Alarma", description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarma configurada para las: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlarmNotificationChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifyAlarm", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notifyAlarm")
                .setSmallIcon(R.drawable.ic_launcher_foreground)  // Asegúrate de tener este recurso en tu proyecto
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
