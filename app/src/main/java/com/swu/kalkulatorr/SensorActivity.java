
package com.swu.kalkulatorr;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SensorActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private TextView textStatusAccelerometer;
    private TextView textAccelerometer;
    private TextView textStatusGyroscope;
    private TextView textGyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textStatusAccelerometer =
                findViewById(R.id.textStatusAccelerometer);
        textAccelerometer =
                findViewById(R.id.textAccelerometer);

        textStatusGyroscope =
                findViewById(R.id.textStatusGyroscope);
        textGyroscope =
                findViewById(R.id.textGyroscope);

        sensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
        );

        gyroscope = sensorManager.getDefaultSensor(
                Sensor.TYPE_GYROSCOPE
        );

        periksaSensor();
    }

    private void periksaSensor() {
        if (accelerometer != null) {
            textStatusAccelerometer.setText(
                    "Status: Sensor tersedia"
            );
        } else {
            textStatusAccelerometer.setText(
                    "Status: Accelerometer tidak tersedia"
            );
            textAccelerometer.setText(
                    "Perangkat tidak memiliki sensor ini."
            );
        }

        if (gyroscope != null) {
            textStatusGyroscope.setText(
                    "Status: Sensor tersedia"
            );
        } else {
            textStatusGyroscope.setText(
                    "Status: Gyroscope tidak tersedia"
            );
            textGyroscope.setText(
                    "Perangkat tidak memiliki sensor ini."
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI
            );
        }

        if (gyroscope != null) {
            sensorManager.registerListener(
                    this,
                    gyroscope,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Hentikan pembacaan sensor saat halaman tidak aktif
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if (event.sensor.getType()
                == Sensor.TYPE_ACCELEROMETER) {

            textAccelerometer.setText(String.format(
                    Locale.US,
                    "Sumbu X : %.2f m/s²\n" +
                            "Sumbu Y : %.2f m/s²\n" +
                            "Sumbu Z : %.2f m/s²",
                    x, y, z
            ));

        } else if (event.sensor.getType()
                == Sensor.TYPE_GYROSCOPE) {

            textGyroscope.setText(String.format(
                    Locale.US,
                    "Sumbu X : %.2f rad/s\n" +
                            "Sumbu Y : %.2f rad/s\n" +
                            "Sumbu Z : %.2f rad/s",
                    x, y, z
            ));
        }
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor, int accuracy) {
        // Tidak memerlukan tindakan khusus
    }
}
