package es.securcom.securso.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.securso.domain.interactor.UpdateEventualDataUseCase
import es.securcom.securso.presentation.data.EventualView
import es.securcom.securso.presentation.plataform.BaseViewModel
import javax.inject.Inject

class UpdateEventualDataViewModel @Inject constructor(private val updateEventualDataUseCase:
                                                      UpdateEventualDataUseCase):
    BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var eventual: EventualView? = null

    fun updateEventualData() = updateEventualDataUseCase(eventual!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}