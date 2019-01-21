package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import es.securcom.secursos.model.persistent.database.entity.DeviceData
import es.securcom.secursos.model.persistent.database.interfaces.DeviceDataDao
import es.securcom.secursos.presentation.data.DeviceView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetDevicesViewModel @Inject constructor(deviceDataDao: DeviceDataDao):
    BaseViewModel() {

    val result: LiveData<List<DeviceView>> = Transformations
        .map(deviceDataDao.getAll(), ::handleMessages)

    private fun handleMessages(list: List<DeviceData>):
            List<DeviceView>{
        val listResult: MutableList<DeviceView> = ArrayList()
        list.forEach { listResult.add(
            DeviceView(it.id, it.phone,
                it.fullName, it.serviceNumber, it.cra_id, it.created_at,
                it.updated_at, it.identifier)
        ) }
        return listResult
    }
}