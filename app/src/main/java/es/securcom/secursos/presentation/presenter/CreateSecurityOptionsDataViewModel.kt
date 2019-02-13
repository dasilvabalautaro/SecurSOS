package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.secursos.domain.interactor.CreateSecurityOptionsDataUseCase
import es.securcom.secursos.presentation.data.SecurityOptionsView
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class CreateSecurityOptionsDataViewModel @Inject constructor(private val createSecurityOptionsDataUseCase:
                                                             CreateSecurityOptionsDataUseCase):
    BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var securityView: SecurityOptionsView? = null

    fun createSecurityOptionsData() = createSecurityOptionsDataUseCase(securityView!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}