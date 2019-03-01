package es.securcom.securso.model.persistent.hardware

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BatteryLevelReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && (intent.action!! == Intent.ACTION_BATTERY_LOW)){

        }

        if (intent != null && (intent.action!! == Intent.ACTION_BATTERY_OKAY)){

        }

    }


}