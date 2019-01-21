package es.securcom.secursos.presentation.presenter

import android.arch.lifecycle.MutableLiveData
import es.securcom.secursos.domain.interactor.PostRepositoryUseCase
import es.securcom.secursos.presentation.plataform.BaseViewModel
import javax.inject.Inject

class PostRepositoryViewModel @Inject constructor(private val postRepositoryUseCase:
                                                  PostRepositoryUseCase):
    BaseViewModel() {

    var result: MutableLiveData<String> = MutableLiveData()

    lateinit var params: List<String>

    fun post() = postRepositoryUseCase(params){
        it.either(::handleFailure, ::handleResult)
    }

    private fun handleResult(value: String){
        result.value = value

    }
}