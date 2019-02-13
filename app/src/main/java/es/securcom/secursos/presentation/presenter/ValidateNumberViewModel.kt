package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import es.securcom.secursos.domain.entity.Body
import es.securcom.secursos.domain.interactor.GetRequestUseCase
import es.securcom.secursos.presentation.data.BodyView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class ValidateNumberViewModel @Inject constructor(private val getRequestUseCase:
                                                  GetRequestUseCase):
    BaseViewModel() {
    private val gSon = Gson()
    var result: MutableLiveData<BodyView> = MutableLiveData()

    var url: String? = null

    fun validate() = getRequestUseCase(url!!){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: Body){

        var jsonStringDevices: String? = null
        var jsonStringCra: String? = null

        if (value.device != null )
            jsonStringDevices = gSon.toJson(value.device)
        if (value.cra != null)
            jsonStringCra = gSon.toJson(value.cra)

        result.value = BodyView(value.error, value.message,
            jsonStringDevices, jsonStringCra)

    }
}