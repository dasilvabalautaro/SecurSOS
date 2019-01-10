package es.securcom.secursos.model.observer

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import es.securcom.secursos.model.services.ManagerLocation
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor():
    LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onEnterDestroy(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onEnterCreate(){
    }


}