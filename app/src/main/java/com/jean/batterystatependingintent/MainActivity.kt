package com.jean.batterystatependingintent

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var batteryPendingIntent: PendingIntent? = null
    private lateinit var batteryStatusReceiver: BatteryStatusReceiver
    private lateinit var textLevel: TextView
    private lateinit var textVoltage: TextView
    private lateinit var textHealth: TextView
    private lateinit var textType: TextView
    private lateinit var textChargingSource: TextView
    private lateinit var textTemperature: TextView
    private lateinit var textChargingStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa los TextViews
        textLevel = findViewById(R.id.textLevel)
        textVoltage = findViewById(R.id.textVoltage)
        textHealth = findViewById(R.id.textHealth)
        textType = findViewById(R.id.textType)
        textChargingSource = findViewById(R.id.textChargingSource)
        textTemperature = findViewById(R.id.textTemperature)
        textChargingStatus = findViewById(R.id.textChargingStatus)

        // Inicializar el BroadcastReceiver
        batteryStatusReceiver = BatteryStatusReceiver(
            textLevel,
            textVoltage,
            textHealth,
            textType,
            textChargingSource,
            textTemperature,
            textChargingStatus
        )
    }

    override fun onResume() {
        super.onResume()

        // Crear el PendingIntent para recibir el estado de la batería
        val batteryStatusIntent = Intent(this, BatteryStatusReceiver::class.java)
        batteryPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            batteryStatusIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Registrar el BroadcastReceiver a través del PendingIntent
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryStatusReceiver, filter)
        Log.d("MainActivity", "BroadcastReceiver registrado con PendingIntent.")
    }

    override fun onPause() {
        super.onPause()

        // Desregistrar el BroadcastReceiver
        unregisterReceiver(batteryStatusReceiver)
        Log.d("MainActivity", "BroadcastReceiver desregistrado.")
    }
}