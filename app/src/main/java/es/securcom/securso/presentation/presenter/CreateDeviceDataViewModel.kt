package es.securcom.securso.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.securso.domain.interactor.CreateDeviceDataUseCase
import es.securcom.securso.presentation.data.DeviceView
import es.securcom.securso.presentation.plataform.BaseViewModel
import javax.inject.Inject

class CreateDeviceDataViewModel @Inject constructor(private val createDeviceDataUseCase:
                                                    CreateDeviceDataUseCase):
    BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var device: DeviceView? = null

    fun createDeviceData() = createDeviceDataUseCase(device!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}