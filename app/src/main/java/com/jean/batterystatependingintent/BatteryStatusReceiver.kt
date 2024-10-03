package com.jean.batterystatependingintent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.TextView

class BatteryStatusReceiver(
    private val textLevel: TextView,
    private val textVoltage: TextView,
    private val textHealth: TextView,
    private val textType: TextView,
    private val textChargingSource: TextView,
    private val textTemperature: TextView,
    private val textChargingStatus: TextView
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BATTERY_CHANGED == intent?.action) {
            // Obtener el nivel de batería
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct: Float = level / scale.toFloat() * 100

            // Obtener el voltaje
            val voltage: Int = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)

            // Obtener la salud de la batería
            val health: Int = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
            val healthString: String = when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Normal"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Sobrecalentado"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Muerta"
                else -> "Desconocida"
            }

            // Obtener el tipo de batería
            val technology: String =
                intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Desconocido"

            // Obtener la fuente de carga
            val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val chargingSource: String = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "Cargando"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> "No cargando"
                else -> "Desconocido"
            }

            // Obtener la temperatura
            val temperature: Int =
                intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 // Convertir a °C

            // Actualizar los TextView
            textLevel.text = "${batteryPct.toInt()}%"
            textVoltage.text = "$voltage mV"
            textHealth.text = healthString
            textType.text = technology
            textChargingSource.text = chargingSource
            textTemperature.text = "${temperature}°C"
            textChargingStatus.text = chargingSource

            // Imprime el estado de la batería en la consola
            Log.d("BatteryStatusReceiver", "Nivel de batería: $batteryPct%")
        }
    }
}