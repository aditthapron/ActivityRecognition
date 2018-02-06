package com.anabella.activity_recognition;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by Bella on 2/4/18.
 */

public class ActivityRecognizedService extends IntentService {


    public ActivityRecognizedService(){
        super("ActivityRecognizedServices");
    }


    public ActivityRecognizedService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        if (ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());

        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities){

        for (DetectedActivity activity : probableActivities){
            switch (activity.getType()){

                case DetectedActivity.RUNNING:{
                    if (activity.getConfidence() > 75) {
                        Log.d("ActivityRecognition", "Running: " + activity.getConfidence());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.updateGraphics(R.drawable.img_running);
                            }
                        });
                    }

                    break;

                }
                case DetectedActivity.STILL:{
                    if (activity.getConfidence() > 75) {
                        Log.d("ActivityRecognition", "Still: " + activity.getConfidence());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.updateGraphics(R.drawable.img_still);
                            }
                        });

                    }
                    break;
                }

                case DetectedActivity.WALKING: {
                    if (activity.getConfidence() > 75) {
                        Log.d("ActivityRecognition", "Walking: " + activity.getConfidence());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.updateGraphics(R.drawable.img_walking);
                            }
                        });

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you walking?");
                        builder.setSmallIcon((R.mipmap.ic_launcher));
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());

                    }
                    break;
                }
                case DetectedActivity.UNKNOWN:{
                    Log.d("ActivityRecognition", "Unknown: " + activity.getConfidence());
                    break;
                }
            }
        }
    }
}
