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

    private SensorManager mSensorManager;
    private Sensor mLight;
    private float mDeclination;
    private float[] mRotationMatrix = new float[16];

   /* public SensorClass(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }*/

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
            //float bearing = Math.toDegrees(orientation[0]) + mDeclination;
            float bearing = Math.toDegrees(orientation[0]) + mDeclination;
            MapsActivity labellemap = new MapsActivity();
            labellemap.updateCamera(bearing);
        }
    }
}
