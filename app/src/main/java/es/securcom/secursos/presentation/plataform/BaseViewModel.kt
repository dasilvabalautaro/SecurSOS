package es.securcom.secursos.presentation.plataform

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.securcom.secursos.model.exception.Failure

abstract class BaseViewModel: ViewModel() {
    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}