package com.example.wearapp.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.IntervalDataPoint
import androidx.health.services.client.data.PassiveListenerConfig
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.Executors


class HealthRepository(private val context: Context) {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val heartRateSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    private val passiveMonitoringClient: PassiveMonitoringClient = HealthServices.getClient(context).passiveMonitoringClient

//    fun getDailySteps(onStepsUpdated: (Long) -> Unit) {
//        Log.d(TAG, "걸음수 트래킹 시작")
//        val config = PassiveListenerConfig.builder()
//            .setDataTypes(setOf(DataType.STEPS_DAILY))
//            .build()
//
//        passiveMonitoringClient.setPassiveListenerCallback(
//            config,
//            Executors.newSingleThreadExecutor(),
//            object : PassiveListenerCallback {
//                override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
//                    val stepsDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
//
//                    stepsDataPoints.forEach { dataPoint ->
//                        val steps = dataPoint.value
//                        onStepsUpdated(steps)
//                        Log.d(TAG, "Steps: $steps")
//                    }
//                }
//            }
//        )
//    }

    fun startTracking(onStepsUpdated: (Long) -> Unit, onDistanceUpdated: (Double) -> Unit, onCaloriesUpdated: (Double) -> Unit) {
        Log.d(TAG, "걸음수, 칼로리 및 이동 거리 트래킹 시작")
        val config = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.STEPS_DAILY, DataType.DISTANCE_DAILY, DataType.CALORIES_DAILY))
            .build()

        passiveMonitoringClient.setPassiveListenerCallback(
            config,
            Executors.newSingleThreadExecutor(),
            object : PassiveListenerCallback {
                override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                    val stepsDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
                    stepsDataPoints.forEach { dataPoint ->
                        val steps = dataPoint.value
                        onStepsUpdated(steps)
                        Log.d(TAG, "Steps: $steps")
                    }

                    val distanceDataPoints = dataPoints.getData(DataType.DISTANCE_DAILY)
                    distanceDataPoints.forEach { dataPoint ->
                        val distance = dataPoint.value
                        onDistanceUpdated(distance)
                        Log.d(TAG, "거리: $distance meters")
                    }

                    val caloriesDataPoints = dataPoints.getData(DataType.CALORIES_DAILY)
                    caloriesDataPoints.forEach { dataPoint ->
                        val calories = dataPoint.value
                        onCaloriesUpdated(calories)
                        Log.d(TAG, "칼로리: $calories")
                    }
                }
            }
        )
    }

    fun startHeartRateMeasurement(onHeartRateUpdated: (Int) -> Unit) {
        heartRateSensor?.also {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val heartRate = event?.values?.firstOrNull()?.toInt()
                    heartRate?.let { onHeartRateUpdated(it) }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Handle sensor accuracy changes if needed
                }
            }, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopHeartRateMeasurement() {
//        sensorManager.unregisterListener(sensorEventListener)
    }

    companion object {
        private const val TAG = "HealthRepository"
    }
}
