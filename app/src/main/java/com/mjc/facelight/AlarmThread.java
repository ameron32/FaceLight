package com.mjc.facelight;

/**
 * Created by Micah Comer on 2/27/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Calendar;

public class AlarmThread extends Thread{

    private SurfaceHolder mSurfaceHolder;
    private Paint blackPaint;
    private Paint lightPaint;
    private Context context;

    int elapsedMinutes = 0;
    int fadeDuration;
    Calendar currentCal;
    int curMin;
    int startMin;
    int alpha;



    //state of game (Running or Paused).
    int state = 1;
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;

    public AlarmThread(SurfaceHolder surfaceHolder, Context context, int fadeDuration) {

        //data about the screen
        mSurfaceHolder = surfaceHolder;
        this.context = context;
        this.fadeDuration = fadeDuration;

        //black painter below to clear the screen before the game is rendered
        blackPaint = new Paint();
        lightPaint = new Paint();
        blackPaint.setARGB(255, 0, 0, 0);
        lightPaint.setARGB(0, 255, 255, 255);
    }

    //This is the most important part of the code. It is invoked when the call to start() is
    //made from the SurfaceView class. It loops continuously until the game is finished or
    //the application is suspended.
    @Override
    public void run() {

        //FOR TESTING:
        fadeDuration = 1;

        //UPDATE
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        if (currentCal == null) {
            currentCal = Calendar.getInstance();
        }
        currentCal.setTimeInMillis(System.currentTimeMillis());
        startMin = currentCal.get(Calendar.MINUTE);


        wl.release();

        while (state==RUNNING) {

            //Calculate alpha of lightPaint.
            //get proportion of elapsed time to fade duration
            //elapsed time = current minutes - start minutes
            curMin = Calendar.getInstance().get(Calendar.MINUTE);
            elapsedMinutes = curMin - startMin;
            if (elapsedMinutes<0){
                elapsedMinutes+=60;
            }

            alpha = (int)(((float)elapsedMinutes/(float)fadeDuration)*255);
            lightPaint.setAlpha(alpha);



            //DRAW
            Canvas c = null;
            try {
                //lock canvas so nothing else can use it
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    //clear the screen with the black painter.
                    c.drawColor(blackPaint.getColor());
                    c.drawColor(lightPaint.getColor());


                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
            //SLEEP
            //Sleep time. Time required to sleep to keep game consistent
            //This starts with the specified delay time (in milliseconds) then subtracts from that the
            //actual time it took to update and render the game. This allows our game to render smoothly.
            try{
                sleep(1000);
            }catch (InterruptedException ie){

            }finally{
                if (elapsedMinutes>=fadeDuration){
                    state = AlarmThread.PAUSED;
                }
            }

        }
    }
}
