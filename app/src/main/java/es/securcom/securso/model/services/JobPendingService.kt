package es.securcom.securso.model.services

import android.app.job.JobParameters
import android.app.job.JobService
import es.securcom.securso.App
import es.securcom.securso.di.ApplicationComponent
import es.securcom.securso.domain.interactor.PostRepositoryUseCase
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.model.persistent.network.entity.PendingShipping
import javax.inject.Inject

class JobPendingService: JobService() {

    val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    lateinit var postRepositoryUseCase: PostRepositoryUseCase

    private lateinit var pending: PendingShipping
    private var index = -1

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        for (i in 0 until Variables.pendingList.size){
            if (Variables.pendingList[i].status == 1){
                pending = Variables.pendingList[i]
                index = i
                break
            }
        }
        if (index != -1){
            sendPending()
        }

        return true
    }

    private fun sendPending(){
        val list = listOf(pending.url, pending.body)
        postRepositoryUseCase(list){
            it.either(::handleFailure, ::handleResult)
        }
    }

    private fun handleResult(value: String){
        Variables.pendingList.removeAt(index)
        index = -1
        println(value)

    }

    private fun handleFailure(failure: Failure) {
        index = -1
        println(failure.toString())
    }
}