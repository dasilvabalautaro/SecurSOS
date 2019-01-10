package es.securcom.secursos

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import com.squareup.leakcanary.LeakCanary
import es.securcom.secursos.di.ApplicationComponent
import es.securcom.secursos.di.ApplicationModule
import es.securcom.secursos.di.DaggerApplicationComponent
import es.securcom.secursos.model.observer.AppLifecycleObserver
import javax.inject.Inject

class App: Application() {
    val component: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }

    private fun injectMembers() = component.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

}