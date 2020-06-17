package it.qbteam.stalkerapp.model.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import it.qbteam.stalkerapp.HomePageActivity;

public class ChronometerPlaceService extends Service {
    private boolean isRunning = false;
    private long startTime = 0;
    private long timeInMilliseconds = 0;
    private long timeSwapBuff = 0;
    private long updatedTime = 0;
    private final IBinder mBinder = new LocalBinder();
    private Message timeMsg;

    public ChronometerPlaceService() { }

    public Runnable updateTimer = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            timeMsg = new Message();
            timeMsg.obj = updatedTime;
            HomePageActivity.sHandler1.sendMessage(timeMsg);
            HomePageActivity.sHandler1.postDelayed(this, 0);
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void startStopPlace(){

        if (isRunning) {
            timeSwapBuff += timeInMilliseconds;
            HomePageActivity.sHandler.removeCallbacks(updateTimer);
            isRunning = false;
            stopSelf();
        } else {
            startTime = SystemClock.uptimeMillis();
            HomePageActivity.sHandler1.postDelayed(updateTimer, 0);
            isRunning = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    public void resetPlace(){

        HomePageActivity.sHandler1.removeCallbacks(updateTimer);
        isRunning=false;
        startTime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;

        timeMsg = new Message();
        timeMsg.obj = updatedTime;
        HomePageActivity.sHandler1.sendMessage(timeMsg);

    }

    public class LocalBinder extends Binder {
        public ChronometerPlaceService getService(){
            return ChronometerPlaceService.this;
        }
    }
}
