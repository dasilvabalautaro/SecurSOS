package es.securcom.securso.presentation.plataform

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.securcom.securso.model.exception.Failure

abstract class BaseViewModel: ViewModel() {
    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}