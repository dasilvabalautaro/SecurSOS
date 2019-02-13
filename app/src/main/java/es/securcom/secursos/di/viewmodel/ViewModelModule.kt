package es.securcom.secursos.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import es.securcom.secursos.presentation.presenter.*

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory:
                                               ViewModelFactory):
            ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GetAlarmCenterViewModel::class)
    abstract fun bindsGetAlarmCenterViewModel(getAlarmCenterViewModel:
                                                  GetAlarmCenterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ValidateNumberViewModel::class)
    abstract fun bindsValidateNumberViewModel(validateNumberViewModel:
                                                  ValidateNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateEventualDataViewModel::class)
    abstract fun bindsCreateEventualDataViewModel(createEventualDataViewModel:
                                                      CreateEventualDataViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CreateAlarmCenterDataViewModel::class)
    abstract fun bindsCreateAlarmCenterDataViewModel(createAlarmCenterDataViewModel:
                                                         CreateAlarmCenterDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateDeviceDataViewModel::class)
    abstract fun bindsCreateDeviceDataViewModel(createDeviceDataViewModel:
                                                    CreateDeviceDataViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(GetEventualDataViewModel::class)
    abstract fun bindsGetEventualDataViewModel(getEventualDataViewModel:
                                                   GetEventualDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GetDevicesViewModel::class)
    abstract fun bindsGetDevicesViewModel(getDevicesViewModel:
                                              GetDevicesViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UpdateEventualDataViewModel::class)
    abstract fun bindsUpdateEventualDataViewModel(updateEventualDataViewModel:
                                                      UpdateEventualDataViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(PostRepositoryViewModel::class)
    abstract fun bindsPostRepositoryViewModel(postRepositoryViewModel:
                                                  PostRepositoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SyncUpViewModel::class)
    abstract fun bindsSyncUpViewModel(syncUpViewModel:
                                          SyncUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateSecurityOptionsDataViewModel::class)
    abstract fun bindsCreateSecurityOptionsDataViewModel(createSecurityOptionsDataViewModel:
                                          CreateSecurityOptionsDataViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(GetSecurityOptionsViewModel::class)
    abstract fun bindsGetSecurityOptionsViewModel(getSecurityOptionsViewModel:
                                                      GetSecurityOptionsViewModel): ViewModel


}