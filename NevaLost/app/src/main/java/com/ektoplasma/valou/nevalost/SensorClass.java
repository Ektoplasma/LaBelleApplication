package com.ektoplasma.valou.nevalost;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Valentin on 17/06/2016.
 */
public class SensorClass implements SensorEventListener {

    private float mDeclination;
    private float[] mRotationMatrix = new float[16];

    public float getmDeclination(){
        return mDeclination;
    }

    public void setmDeclination(float VDeclination){
        mDeclination = VDeclination;
    }

    public float[] getmRotationMatrix(){
        return mRotationMatrix;
    }

    public void mRotationMatrix(float[] VRotationMatrix){
        mRotationMatrix = VRotationMatrix;
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1){
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = (float) (Math.toDegrees(orientation[0]) + mDeclination);
            if(ProfileHead.isCarry()) {
                CreatorMapsActivity labellemap = new CreatorMapsActivity();
                labellemap.updateCamera(bearing);
            }
            else{
                FollowerMapsActivity labellemap = new FollowerMapsActivity();
                labellemap.updateCamera(bearing);
            }
        }
    }
}
