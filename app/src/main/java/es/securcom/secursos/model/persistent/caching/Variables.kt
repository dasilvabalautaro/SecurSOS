package es.securcom.secursos.model.persistent.caching

import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import es.securcom.secursos.presentation.data.*

object Variables {
    var alarmCenter: AlarmCenterView? = null
    var eventualData: EventualView? = null
    var devicesView: DeviceView? = null
    var securityButtonViewSelected: SecurityOptionsView? = null
    val pendingList: MutableList<PendingShipping> = mutableListOf()
    val securityOptionList: MutableList<SecurityOptionsView> = mutableListOf()
    enum class LastSentEvent(val event: String) {
        CANCEL("CANCEL PREALARM"),
        DEAD("DEAD MAN"),
        TIMEOUT("PREALARM + Timeout"),
        IMAGE("IMAGE"),
        AUDIO("AUDIO"),
        INIT_DEAD("HOMBRE MUERTO INICIADO"),
        SEND_ALARM("SEND ALARM"),
        SUCCESS("SUCCESS_ACTION"),
        ERROR("ERROR_ACTION"),
        NO_RESPONSE("NO_RESPONSE_ACTION"),
        MAX_RETRIES("MAX_RETRIES_ACTION"),
        SAVE_RECORDER("SAVE_RECORDER")
    }
}