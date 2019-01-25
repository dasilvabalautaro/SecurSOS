package es.securcom.secursos.presentation.view.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Secure.getString
import android.view.View
import es.securcom.secursos.App
import es.securcom.secursos.R
import es.securcom.secursos.extension.observe
import es.securcom.secursos.extension.failure
import es.securcom.secursos.extension.viewModel
import es.securcom.secursos.model.persistent.caching.Constants
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.presentation.data.AlarmCenterView
import es.securcom.secursos.presentation.data.BodyView
import es.securcom.secursos.presentation.data.DeviceView
import es.securcom.secursos.presentation.plataform.BaseFragment
import es.securcom.secursos.presentation.presenter.*
import es.securcom.secursos.presentation.tools.Conversion
import kotlinx.android.synthetic.main.view_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class RegisterFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles
    private lateinit var validateNumberViewModel: ValidateNumberViewModel
    private lateinit var getAlarmCenterViewModel: GetAlarmCenterViewModel
    private lateinit var createAlarmCenterDataViewModel: CreateAlarmCenterDataViewModel
    private lateinit var createDeviceDataViewModel: CreateDeviceDataViewModel
    private lateinit var getDevicesViewModel: GetDevicesViewModel

    override fun layoutId() = R.layout.view_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        getAlarmCenterViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultAlarmCenter)
            failure(failure, ::handleFailure)
        }

        getDevicesViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultDevices)
            failure(failure, ::handleFailure)
        }

        validateNumberViewModel = viewModel(viewModelFactory) {
            observe(result, ::handleGetValidate)
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

        manageFiles.verifyLog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flagInit = false
        bt_validate.setOnClickListener { verifyNumber() }
        println("STATUS: ${manageFiles.statusLog}")

    }

    private fun resultAlarmCenter(list: List<AlarmCenterView>?){
        if (!list.isNullOrEmpty()){
            Variables.alarmCenter = list[0]
            (activity!!.application as App).navigator.showMain(activity!!)
        }
    }

    private fun resultDevices(list: List<DeviceView>?){
        if (!list.isNullOrEmpty()){
            Variables.devicesView = list[0]

        }
    }

    private fun resultCreateAlarm(value: Boolean?){
        println(value.toString())
    }

    private fun handleGetValidate(value: BodyView?){
        et_number.setText("")
        if (value != null){
            if (value.error){
                context!!.toast("Error: ${value.error}\nMessage: ${value.message}")
                (activity!!.application as App).navigator.showMain(activity!!)
            }else{
                if (value.cra != null && value.device != null){
                    createAlarmCenterData(value.cra)
                    createDeviceData(value.device)
                    (activity!!.application as App).navigator.showMain(activity!!)
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


    private fun verifyNumber(){
        val url = getUrl()
        if (url != null){
            validateNumberViewModel.url = url
            validateNumberViewModel.validate()
        }else{
            context!!.toast(getString(R.string.lbl_field_empty))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getUrl(): String?{
        return if (et_number.text.isNotEmpty()){
            val servicesNumber = et_number.text.toString()
            val deviceId = getString(context!!.contentResolver,
                Settings.Secure.ANDROID_ID)
            val stringLog = "Sincronizando Automáticamente. No. Servicio: $servicesNumber, Ident: $deviceId"
            manageFiles.writeFile(manageFiles.fileLogName, stringLog)
            //val deviceId = "31487b45121b369a"
            val url = String.format("${Constants.urlBase}${Constants.deviceServiceNumber}" +
                    "$servicesNumber/$deviceId")
            url

        }else{
            null
        }

    }

    override fun renderFailure(message: Int) {
        notify(message)
    }

}