package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.secursos.domain.entity.Body
import es.securcom.secursos.domain.interactor.GetRequestUseCase
import es.securcom.secursos.presentation.data.BodyView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class ValidateNumberViewModel @Inject constructor(private val getRequestUseCase:
                                                  GetRequestUseCase):
    BaseViewModel() {
    var result: MutableLiveData<BodyView> = MutableLiveData()

    var url: String? = null

    fun validate() = getRequestUseCase(url!!){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: Body){
        result.value = BodyView(value.error, value.message,
            value.device, value.cra)

    }
}