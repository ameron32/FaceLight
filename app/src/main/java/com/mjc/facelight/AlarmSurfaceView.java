package com.mjc.facelight;

import java.io.Serializable;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AlarmSurfaceView extends SurfaceView implements Serializable, SurfaceHolder.Callback{

    SurfaceHolder surfaceHolder;
    Context context;
    int fadeDuration;

    public void setFadeDuration(int fadeDuration) {
        this.fadeDuration = fadeDuration;
    }
    private AlarmThread thread;

    //class constructors
    public AlarmSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        InitView();

    }
    public AlarmSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        InitView();
    }
    public AlarmSurfaceView(Context context) {
        super(context);

        this.context = context;

        InitView();

    }

    public void InitView(){
        surfaceHolder = getHolder();
        surfaceHolder.addCallback( this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

    }

    //region Event Handlers

    //Surface Interface Methods
    //@Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}


    public void surfaceCreated(SurfaceHolder arg0) {
            thread = new AlarmThread(surfaceHolder, context, fadeDuration);
            thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        while (retry) {
            try {
                //code to kill Thread
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    public void touched(){
        thread.interrupt();
    }

    //endregion


}


