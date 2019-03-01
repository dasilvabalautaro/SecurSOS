package es.securcom.securso.model.persistent.hardware

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckTelephony @Inject constructor(private val context: Context) {

    var signalStrengthValue = 0

    private val phoneListener = object : PhoneStateListener() {
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            signalStrengthValue = signalStrength.gsmSignalStrength * 100 / 31
            super.onSignalStrengthsChanged(signalStrength)
        }
    }

    fun startSignalLevelListener() {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }

    fun stopSignalLevelListener() {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(phoneListener, PhoneStateListener.LISTEN_NONE)
    }
}