package es.securcom.secursos.di

import android.content.Context
import dagger.Component
import es.securcom.secursos.App
import es.securcom.secursos.di.viewmodel.ViewModelModule
import es.securcom.secursos.model.services.JobLocationService
import es.securcom.secursos.presentation.view.activity.RegisterActivity
import es.securcom.secursos.presentation.view.activity.RouteActivity
import es.securcom.secursos.presentation.view.fragment.RegisterFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(app: App)
    fun context(): Context
    fun inject(routeActivity: RouteActivity)
    fun inject(registerFragment: RegisterFragment)
    fun inject(registerActivity: RegisterActivity)
}