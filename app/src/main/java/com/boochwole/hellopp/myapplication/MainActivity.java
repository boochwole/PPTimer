package com.boochwole.hellopp.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    static long btwTime=9;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button targetName = (Button) findViewById(R.id.btnTargetName);
        final TextView dateNum = (TextView) findViewById(R.id.txtvlDateNum);
        final Calendar today = Calendar.getInstance();
        final Intent intentService = new Intent(this, DesktopTestService.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String targetName_s = preferences.getString("target_name", null);
        targetName.setText(targetName_s);

        final long targetDateInit = preferences.getLong("target_date", 0);
        final long todayDateInit = caculateTimstamp(today.getTimeInMillis());
        if(targetDateInit == 0)
        {
            dateNum.setText("100");
        }
        else {
            btwTime = (targetDateInit - todayDateInit) / 1000 / 60 / 60 / 24;
            dateNum.setText(String.valueOf(btwTime));
            startService(intentService);
        }


        targetName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date tempDate =new Date(preferences.getLong("target_date", today.getTimeInMillis()));
                String sdfTemp = simpleDateFormat.format(tempDate);
                Toast toast = Toast.makeText(MainActivity.this, sdfTemp, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        dateNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout settingPage = (TableLayout)getLayoutInflater().inflate(R.layout.setting_page, null);
                final EditText dtTargetName = (EditText)settingPage.findViewById(R.id.dttxtTargetName);
                final DatePicker dpTargetDate = (DatePicker)settingPage.findViewById(R.id.dpTargetDate);
                final Calendar targetDate = Calendar.getInstance();
                final SharedPreferences.Editor editor = preferences.edit();
                dtTargetName.setText(preferences.getString("target_name", null));

                /*将SharedPreferences中储存的目标时间换算成年月日，并显示在dtTargetDate中*/
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date tempDate =new Date(preferences.getLong("target_date", caculateTimstamp(today.getTimeInMillis())));
                String sdfTemp = simpleDateFormat.format(tempDate);
                int firstIndex = sdfTemp.indexOf("-");
                int lastIndex = sdfTemp.lastIndexOf("-");
                int year = Integer.parseInt(sdfTemp.substring(0, firstIndex));
                int month = Integer.parseInt(sdfTemp.substring(firstIndex+1,lastIndex))-1;//显示的是正常从1开始的月份，而使用的是从0开始的月份
                int day = Integer.parseInt(sdfTemp.substring(lastIndex+1));

                dpTargetDate.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        }
                });

                new AlertDialog.Builder(MainActivity.this)
                        .setView(settingPage)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String targetNameTemp = dtTargetName.getText().toString();
                                targetName.setText(targetNameTemp);
                                editor.putString("target_name", targetNameTemp);
                                int yearTemp = dpTargetDate.getYear();
                                int monthTemp = dpTargetDate.getMonth();
                                int dayTemp = dpTargetDate.getDayOfMonth();
                                targetDate.set(yearTemp, monthTemp, dayTemp);
                                long cacTargetDate = caculateTimstamp(targetDate.getTimeInMillis());
                                editor.putLong("target_date", cacTargetDate);
                                long cacTodayDate = caculateTimstamp(today.getTimeInMillis());
                                btwTime = (cacTargetDate - cacTodayDate) / 1000 / 60 / 60 / 24;
                                dateNum.setText(String.valueOf(btwTime));
                                editor.putLong("btw_num", btwTime);
                                editor.commit();
                                startService(intentService);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which){

                            }
                        })
                        .create()
                        .show();
                        }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button targetName = (Button) findViewById(R.id.btnTargetName);
        final TextView dateNum = (TextView) findViewById(R.id.txtvlDateNum);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String targetName_s = preferences.getString("target_name", null);
        targetName.setText(targetName_s);
        final Calendar today = Calendar.getInstance();
        long targetDateInit = preferences.getLong("target_date", 0);
        long todayDateInit = caculateTimstamp(today.getTimeInMillis());
        if(targetDateInit == 0)
        {
            dateNum.setText("100");
        }
        else {
            btwTime = (targetDateInit - todayDateInit) / 1000 / 60 / 60 / 24;
            dateNum.setText(String.valueOf(btwTime));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public long caculateTimstamp(long time)
    {
        Calendar timestamp = Calendar.getInstance();
        timestamp.setTimeInMillis(time);
        timestamp.set(Calendar.MILLISECOND,0);
        timestamp.set(Calendar.SECOND,0);
        timestamp.set(Calendar.MINUTE,0);
        timestamp.set(Calendar.HOUR_OF_DAY,0);
        return timestamp.getTimeInMillis();
    }
}
