package co.edu.udea.compumovil.gr10_20172.lab4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            Intent serviceIntent = new Intent(context, MyService.class);
            //context.startService(serviceIntent);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
                    serviceIntent, 0);

            //setup calendar object for alarm start time
            Calendar cal= Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());


            //make the alarm
            AlarmManager morningAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            int timerInterval = sharedPref.getInt("edit_timer", 60);

            //(type, starttime, interval, pintent)
            morningAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), timerInterval*1000, pIntent);
        }
    }
}
