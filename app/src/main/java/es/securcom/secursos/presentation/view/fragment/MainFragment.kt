package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.secursos.R
import es.securcom.secursos.extension.failure
import es.securcom.secursos.extension.observe
import es.securcom.secursos.extension.viewModel
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.presentation.data.AlarmCenterView
import es.securcom.secursos.presentation.data.BodyView
import es.securcom.secursos.presentation.data.DeviceView
import es.securcom.secursos.presentation.data.EventualView
import es.securcom.secursos.presentation.plataform.BaseFragment
import es.securcom.secursos.presentation.presenter.*
import es.securcom.secursos.presentation.tools.Conversion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles
    private lateinit var getEventualDataViewModel: GetEventualDataViewModel
    private lateinit var createEventualDataViewModel: CreateEventualDataViewModel
    private lateinit var getAlarmCenterViewModel: GetAlarmCenterViewModel
    private lateinit var getDevicesViewModel: GetDevicesViewModel
    private lateinit var syncUpViewModel: SyncUpViewModel
    private lateinit var createAlarmCenterDataViewModel: CreateAlarmCenterDataViewModel
    private lateinit var createDeviceDataViewModel: CreateDeviceDataViewModel

    override fun layoutId() = R.layout.view_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        if (flagInit){
            getAlarmCenterViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultAlarmCenter)
                failure(failure, ::handleFailure)
            }
            getDevicesViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultDevices)
                failure(failure, ::handleFailure)
            }

            syncUpViewModel = viewModel(viewModelFactory) {
                observe(result, ::handleSyncUp)
                failure(failure, ::handleFailure)
            }
            createAlarmCenterDataViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultCreateAlarm)
                failure(failure, ::handleFailure)
            }

            createDeviceDataViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultCreateAlarm)
                failure(failure, ::handleFailure)
            }

        }

        getEventualDataViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultEventualData)
            failure(failure, ::handleFailure)
        }

        createEventualDataViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultCreateEventual)
            failure(failure, ::handleFailure)
        }

        manageFiles.verifyLog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (flagInit){
            syncUp()
            flagInit = false
        }


    }

    private fun resultCreateEventual(value: Boolean?){
        println(value.toString())
    }

    private fun resultDevices(list: List<DeviceView>?){
        if (!list.isNullOrEmpty()){
            Variables.devicesView = list[0]

        }
    }

    private fun resultCreateAlarm(value: Boolean?){
        println(value.toString())
    }

    private fun resultAlarmCenter(list: List<AlarmCenterView>?){
        if (!list.isNullOrEmpty()){
            Variables.alarmCenter = list[0]
        }
    }

    private fun resultEventualData(list: List<EventualView>?){
        if (!list.isNullOrEmpty()){
            Variables.eventualData = list[0]

        }else{
            val eventualView = EventualView(0, 0f,
                0f,0.0,0.0,0f,0.0, 0)
            createEventualDataViewModel.eventual = eventualView
            createEventualDataViewModel.createEventualData()
        }
    }

    private fun handleSyncUp(value: BodyView?){
        if (value != null){
            if (value.error){
                context!!.toast("Error: ${value.error}\nMessage: ${value.message}")
            }else{
                if (value.cra != null && value.device != null){
                    createAlarmCenterData(value.cra)
                    createDeviceData(value.device)

                }else{
                    context!!.toast(getString(R.string.lbl_entity_empty))
                }
            }
        }
    }

    private fun createAlarmCenterData(cra: String){
        GlobalScope.launch {
            val acv = Conversion.matchAlarmCenterView(cra)
            if (acv != null){
                createAlarmCenterDataViewModel.acv = acv
                createAlarmCenterDataViewModel.createAlarmCenterData()

            }

        }
    }

    private fun createDeviceData(device: String){
        GlobalScope.launch {
            val dev = Conversion.matchDeviceView(device)
            if (dev != null){
                createDeviceDataViewModel.device = dev
                createDeviceDataViewModel.createDeviceData()

            }

        }
    }

    private fun syncUp(){
        val url = Conversion.getUrlIdentifier()
        syncUpViewModel.url = url
        syncUpViewModel.syncUp()
    }

    override fun renderFailure(message: Int) {
        notify(message)
    }



}