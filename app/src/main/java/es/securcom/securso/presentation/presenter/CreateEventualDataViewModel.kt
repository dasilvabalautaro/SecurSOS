package es.securcom.securso.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.securso.domain.interactor.CreateEventualDataUseCase
import es.securcom.securso.presentation.data.EventualView
import es.securcom.securso.presentation.plataform.BaseViewModel
import javax.inject.Inject

class CreateEventualDataViewModel @Inject constructor(private val createEventualDataUseCase:
                                                      CreateEventualDataUseCase):
    BaseViewModel() {

    val result: MutableLiveData<Boolean> = MutableLiveData()

    var eventual: EventualView? = null

    fun createEventualData() = createEventualDataUseCase(eventual!!){
        it.either(::handleFailure, ::handleResult)}

    private fun handleResult(value: Boolean){

        result.value = value

    }
}