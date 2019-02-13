package es.securcom.secursos.di

import android.content.Context
import dagger.Component
import es.securcom.secursos.App
import es.securcom.secursos.di.viewmodel.ViewModelModule
import es.securcom.secursos.model.persistent.network.LocationListener
import es.securcom.secursos.model.services.JobPendingService
import es.securcom.secursos.presentation.view.activity.*
import es.securcom.secursos.presentation.view.fragment.*
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