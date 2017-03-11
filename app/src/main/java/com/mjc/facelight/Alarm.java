package com.mjc.facelight;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Micah Comer on 2/22/2017.
 */

public class Alarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        fade(context, intent.getIntExtra("Duration", 0));
    }

    public void setAlarm(Context c, int duration, Calendar cal)
    {
        AlarmManager am =( AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(c, Alarm.class);
        i.putExtra("Duration", duration);
        PendingIntent pi = PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void fade(Context context, int duration){
        Log.d("Light Screen", "Fade executed");
        Intent i = new Intent(context, LightScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("Duration", duration);
        context.startActivity(i);
    }
}
