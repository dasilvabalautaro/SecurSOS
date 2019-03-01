package es.securcom.securso.model.persistent.hardware

import android.content.BroadcastReceiver
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Singleton
class CheckBattery @Inject constructor(context: Context) {

    val batteryStatusReceiver = BatteryStatusReceiver()
    private val iFilter: IntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    private val batteryStatus = context.registerReceiver(batteryStatusReceiver, iFilter)

    fun stateOfCharge(): Int{
        return batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    }

    fun isCharging(): Boolean{
        val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }

    /*fun level(): Float{
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return level / scale.toFloat()
    }
*/
    class BatteryStatusReceiver: BroadcastReceiver(){

        var status: Float = 0f
        var observableBatteryStatus: Subject<Float> = PublishSubject.create()

        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            status = level / scale.toFloat()
            observableBatteryStatus.onNext(status)
        }

    }
}