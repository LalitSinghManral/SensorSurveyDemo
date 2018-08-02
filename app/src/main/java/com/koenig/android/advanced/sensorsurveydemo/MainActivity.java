package com.koenig.android.advanced.sensorsurveydemo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private List<Sensor> mSensor_list;
    //private TextView mSensorTextView;
    private TextView mLightSensorText;
    private TextView mProximityText;
    private ImageView mImageView;

    //Sensors Object
    private Sensor mLightSensor;
    private Sensor mProximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.sensor_second_layout); //new layout file

        //mSensorTextView = findViewById(R.id.sensor_list);
        mLightSensorText = findViewById(R.id.label_light);
        mProximityText = findViewById(R.id.label_proximity);
        mImageView = findViewById(R.id.label_image);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor_list = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mLightSensor == null){
            mLightSensorText.setText(sensor_error);
        }

        if (mProximitySensor == null){
            mProximityText.setText(sensor_error);
        }

        StringBuilder sensorText = new StringBuilder();
        for (Sensor sensor: mSensor_list){
            sensorText.append(sensor.getName()).append("\n");
        }

        //mSensorTextView.setText(sensorText.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLightSensor != null){
            mSensorManager.registerListener(this,mLightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mProximitySensor != null){
            mSensorManager.registerListener(this,mProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        float sensorValue = sensorEvent.values[0];

        switch (sensorType){
            case Sensor.TYPE_LIGHT :
                mLightSensorText.setText(getResources().getString(R.string.light_text,
                        sensorValue));
                break;

            case Sensor.TYPE_PROXIMITY :
                mProximityText.setText(getResources().getString(R.string.proximity_text,
                        sensorValue));
                if (sensorValue == 0){
                    mImageView.setImageResource(R.drawable.near);
                }
                else {
                    mImageView.setImageResource(R.drawable.far);
                }
                break;

                default:
                    //Nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do Nothing
        int typeSensor = sensor.getType();
        switch (typeSensor){
            case Sensor.TYPE_LIGHT :
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                break;

            case Sensor.TYPE_PROXIMITY :

                break;

            default:
                //Nothing
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }
}
