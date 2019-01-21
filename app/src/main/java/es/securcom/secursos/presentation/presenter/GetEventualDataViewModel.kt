package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import es.securcom.secursos.model.persistent.database.entity.EventualData
import es.securcom.secursos.model.persistent.database.interfaces.EventualDataDao
import es.securcom.secursos.presentation.data.EventualView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetEventualDataViewModel @Inject constructor(eventualDataDao:
                                                   EventualDataDao):
    BaseViewModel() {
    val result: LiveData<List<EventualView>> = Transformations
        .map(eventualDataDao.getAll(), ::handleMessages)

    private fun handleMessages(list: List<EventualData>):
            List<EventualView>{
        val listResult: MutableList<EventualView> = ArrayList()
        list.forEach { listResult.add(
            EventualView(it.id, it.batteryLevel,
                it.speed, it.latitude, it.longitude, it.heading,
                it.lastUpdate, it.lowBatteryAlarmValue)
        ) }
        return listResult
    }
}