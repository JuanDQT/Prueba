package com.example.pruebatecnica

import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private var mLightSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ir a api reconocimiento facial..
        if (false) {
            startActivity(Intent(this, MainActivity::class.java))
            return;
        }

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) as Sensor

        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            info.text = "${it.values[0]} % brillo"
        }
    }
}