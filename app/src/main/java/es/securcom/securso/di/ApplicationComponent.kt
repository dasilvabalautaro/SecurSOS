package es.securcom.securso.di

import android.content.Context
import dagger.Component
import es.securcom.securso.App
import es.securcom.securso.di.viewmodel.ViewModelModule
import es.securcom.securso.model.persistent.network.LocationListener
import es.securcom.securso.model.services.JobPendingService
import es.securcom.securso.presentation.view.activity.*
import es.securcom.securso.presentation.view.fragment.*
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun context(): Context
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(connectionFragment: ConnectionFragment)
    fun inject(testActivity: TestActivity)
    fun inject(testFragment: TestFragment)
    fun inject(registerActivity: RegisterActivity)
    fun inject(registerFragment: RegisterFragment)
    fun inject(locationListener: LocationListener)
    fun inject(messageFragment: MessageFragment)
    fun inject(alarmFragment: AlarmFragment)
    fun inject(logFragment: LogFragment)
    fun inject(jobPendingService: JobPendingService)
    fun inject(alarmActivity: AlarmActivity)
    fun inject(informationFragment: InformationFragment)
}