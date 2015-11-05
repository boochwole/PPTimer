package com.boochwole.hellopp.myapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.util.Log;
import android.widget.RemoteViews;
import android.content.*;


/**
 * Created by BB on 2015/10/17.
 */
public class DesktopTest extends AppWidgetProvider
{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d("kanzheli", "onUpdate");
        Intent intentService = new Intent(context, DesktopTestService.class);
        context.startService(intentService);
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        Log.d("kanzheli", "onReceive");
    }
}
