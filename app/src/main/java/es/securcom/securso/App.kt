package es.securcom.securso

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.res.Configuration
import com.squareup.leakcanary.LeakCanary
import es.securcom.securso.di.ApplicationComponent
import es.securcom.securso.di.ApplicationModule
import es.securcom.securso.di.DaggerApplicationComponent
import es.securcom.securso.model.observer.AppLifecycleObserver
import es.securcom.securso.presentation.navigation.Navigator
import es.securcom.securso.presentation.tools.LocaleUtils
import java.util.*
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
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var localeUtils: LocaleUtils


    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        val language = Locale.getDefault().language
        localeUtils.setLocale(Locale(language))
        localeUtils.updateConfiguration(this,
            baseContext.resources.configuration)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localeUtils.updateConfiguration(this, newConfig!!)
    }

    private fun injectMembers() = component.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

}