package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import es.securcom.secursos.model.persistent.database.entity.AlarmCenterData
import es.securcom.secursos.model.persistent.database.interfaces.AlarmCenterDataDao
import es.securcom.secursos.presentation.data.AlarmCenterView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetAlarmCenterViewModel @Inject constructor(alarmCenterDataDao:
                                                  AlarmCenterDataDao):
    BaseViewModel() {
    val result: LiveData<List<AlarmCenterView>> = Transformations
        .map(alarmCenterDataDao.getAll(), ::handleMessages)

    private fun handleMessages(list: List<AlarmCenterData>):
            List<AlarmCenterView>{
        val listResult: MutableList<AlarmCenterView> = ArrayList()
        list.forEach { listResult.add(AlarmCenterView(it.id, it.name,
            it.phone, it.email, it.logo, it.color, it.web, it.ipPrimary,
            it.portPrimary, it.ipSecondary, it.portSecondary, it.created,
            it.updated, it.information, it.lapseLocation, it.latLngType,
            it.lowBatteryAlert, it.lowBatteryAlertValue, it.lowBatteryAlarm,
            it.lowBatteryAlarmValue, it.lowSignalAlert, it.lowSignalAlertValue,
            it.line)) }
        return listResult
    }
}