package com.boochwole.hellopp.myapplication;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Created by BB on 2015/10/19.
 */
public class DesktopTestService extends IntentService
{
    public DesktopTestService()
    {
        super("DesktopTestService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("kanzheli", "service has been Handled");
        desktopUpdate();
    }

    private void desktopUpdate()
    {
        Calendar today = Calendar.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String targetName = preferences.getString("target_name", null);
        long targetDate = preferences.getLong("target_date", 0);
        long todayDate = caculateTimstamp(today.getTimeInMillis());
        long btwNum = (targetDate - todayDate) / 1000 / 60 / 60 / 24;

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),R.layout.desktop_test);
        ComponentName componentName = new ComponentName(this,DesktopTest.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        remoteViews.setTextViewText(R.id.txtvlTargetName_dt,targetName);
        remoteViews.setTextViewText(R.id.txtvlDateNum_dt, String.valueOf(btwNum));
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        Log.d("kanzheli", String.valueOf(btwNum));
        Log.d("kanzheli", "service have been updated");
    }
    public long caculateTimstamp(long time)
    {
        Calendar timestamp = Calendar.getInstance();
        timestamp.setTimeInMillis(time);
        timestamp.set(Calendar.MILLISECOND, 0);
        timestamp.set(Calendar.SECOND, 0);
        timestamp.set(Calendar.MINUTE, 0);
        timestamp.set(Calendar.HOUR_OF_DAY, 0);
        return timestamp.getTimeInMillis();
    }

}
