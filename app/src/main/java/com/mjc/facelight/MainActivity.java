package com.mjc.facelight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Users can interface with only THIS one screen.
 *
 * Options to be on this screen:
 *   Alarm Time (time picker)
 *   Light Fade Duration (slider: 5, 10, 15, 20, 25, or 30 minutes)
 *   Sound Fade Duration (slider: 1, 2, 3, 4, or 5 minutes)
 *   Alarm On/Off Toggle
 *   Done Button
 */
public class MainActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, SeekBar.OnSeekBarChangeListener {

    TimePicker tp;
    SeekBar sb;
    TextView fadeInTimeTextView;
    Alarm alarm;
    int seekBarAmt;
    TextView doneTextView;
    boolean enabled = true;
    CheckBox toggle;
    Calendar alarmCalendar;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = getApplicationContext();

        tp = (TimePicker)findViewById(R.id.timePicker);
        tp.setOnTimeChangedListener(this);

        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(this);
        fadeInTimeTextView = (TextView)findViewById(R.id.fadeInTimeAmount_tv);
        doneTextView = (TextView)findViewById(R.id.done_tv);

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled)
                alarm.setAlarm(c, seekBarAmt, alarmCalendar);
                moveTaskToBack(true);
            }
        });
        toggle = (CheckBox)findViewById(R.id.checkbox);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        seekBarAmt = sb.getProgress();

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            if (alarm == null)
                alarm = new Alarm();

            alarm.cancelAlarm(this);

            if (alarmCalendar == null){
                alarmCalendar = Calendar.getInstance();
            }
            alarmCalendar.setTimeInMillis(System.currentTimeMillis());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarmCalendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarAmt = progress+1;
        fadeInTimeTextView.setText("Fade In Duration: "+ String.valueOf(seekBarAmt * 5)+" min");
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void toggle(){
        enabled = !enabled;
    }
}
