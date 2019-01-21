package es.securcom.secursos.model.persistent.caching

import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import es.securcom.secursos.presentation.data.*

object Variables {
    var alarmCenter: AlarmCenterView? = null
    var eventualData: EventualView? = null
    var devicesView: DeviceView? = null
    var securityButtonView: SecurityButtonView? = null
    val pendingList: MutableList<PendingShipping> = mutableListOf()
}