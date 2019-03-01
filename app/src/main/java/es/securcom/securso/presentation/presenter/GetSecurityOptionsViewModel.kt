package es.securcom.securso.presentation.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import es.securcom.securso.model.persistent.database.entity.SecurityOptionsData
import es.securcom.securso.model.persistent.database.interfaces.SecurityOptionsDataDao
import es.securcom.securso.presentation.data.SecurityOptionsView
import es.securcom.securso.presentation.plataform.BaseViewModel
import javax.inject.Inject

class GetSecurityOptionsViewModel @Inject constructor(securityOptionsDataDao:
                                                      SecurityOptionsDataDao):
    BaseViewModel() {

    val result: LiveData<List<SecurityOptionsView>> = Transformations
        .map(securityOptionsDataDao.getAll(), ::handleMessages)

    private fun handleMessages(list: List<SecurityOptionsData>):
            List<SecurityOptionsView>{
        val listResult: MutableList<SecurityOptionsView> = ArrayList()
        list.forEach { listResult.add(SecurityOptionsView(it.pk, it.id,
            it.craId, it.evcode, it.descr, it.icon, it.color, it.time,
            it.count, it.audio, it.snap, it.delayedAlarm, it.notificationPre,
            it.tiempodefectodemoraminutos, it.hombremuerto, it.hombremuertovalor,
            it.calendariolunes, it.calendariomartes, it.calendariomiercoles,
            it.calendariojueves, it.calendarioviernes, it.calendariosabado,
            it.calendariodomingo, it.calendariohoraini1, it.calendariohorafin1,
            it.calendariohoraini2, it.calendariohorafin2)) }
        return listResult
    }
}