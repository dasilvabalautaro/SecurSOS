package es.securcom.securso.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.securso.domain.interactor.CreateAlarmCenterDataUseCase
import es.securcom.securso.presentation.data.AlarmCenterView
import es.securcom.securso.presentation.plataform.BaseViewModel
import javax.inject.Inject

class CreateAlarmCenterDataViewModel @Inject constructor(private val createAlarmCenterDataUseCase:
                                                         CreateAlarmCenterDataUseCase):
    BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var acv: AlarmCenterView? = null

    fun createAlarmCenterData() = createAlarmCenterDataUseCase(acv!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}