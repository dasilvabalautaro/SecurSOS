package es.securcom.secursos.model.observer

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.widget.Toast
import es.securcom.secursos.model.services.ManagerLocation
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(private val context: Context):
    LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onEnterDestroy(){
        Toast.makeText(context, "Hasta luego", Toast.LENGTH_LONG).show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onEnterCreate(){
        Toast.makeText(context, "Iniciando", Toast.LENGTH_LONG).show()
    }


}