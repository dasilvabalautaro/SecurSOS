package es.securcom.securso.presentation.view.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Secure.getString
import android.view.View
import es.securcom.securso.App
import es.securcom.securso.R
import es.securcom.securso.extension.observe
import es.securcom.securso.extension.failure
import es.securcom.securso.extension.viewModel
import es.securcom.securso.model.persistent.caching.Constants
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.model.persistent.files.ManageFiles
import es.securcom.securso.model.persistent.preference.PreferenceRepository
import es.securcom.securso.model.persistent.preference.PreferenceRepository.set
import es.securcom.securso.presentation.data.AlarmCenterView
import es.securcom.securso.presentation.data.BodyView
import es.securcom.securso.presentation.data.DeviceView
import es.securcom.securso.presentation.data.SecurityOptionsView
import es.securcom.securso.presentation.plataform.BaseFragment
import es.securcom.securso.presentation.presenter.*
import es.securcom.securso.presentation.tools.Conversion
import es.securcom.securso.presentation.view.activity.RegisterActivity
import io.reactivex.schedulers.Schedulers
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
    private lateinit var createSecurityOptionsDataViewModel: CreateSecurityOptionsDataViewModel
    private lateinit var getSecurityOptionsViewModel: GetSecurityOptionsViewModel
    private val defaultButton = 0

    override fun layoutId() = R.layout.view_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        val countFill = observableFillObject.map { c -> c }
        disposable.add(countFill.observeOn(Schedulers.newThread())
            .subscribe {
                if (it == 2){
                    countFillObject = 0
                    sendPostInit()
                }
            })

        getAlarmCenterViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultAlarmCenter)
            failure(failure, ::handleFailure)
        }

        getDevicesViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultDevices)
            failure(failure, ::handleFailure)
        }
        getSecurityOptionsViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSecurityOptions)
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
            observe(result, ::resultCreateDevice)
            failure(failure, ::handleFailure)
        }

        createSecurityOptionsDataViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultCreateSecurityOptions)
            failure(failure, ::handleFailure)
        }

        postRepositoryViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendPost)
            failure(failure, ::handleFailure)
        }

        manageFiles.verifyLog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flagInit = false
        bt_validate.setOnClickListener {
            verifyNumber()
        }

    }

    private fun resultAlarmCenter(list: List<AlarmCenterView>?){
        if (!list.isNullOrEmpty()){
            Variables.alarmCenter = list[0]
            countFillObject ++
            this.observableFillObject.onNext(countFillObject)
        }
    }

    private fun resultDevices(list: List<DeviceView>?){
        if (!list.isNullOrEmpty()){
            Variables.devicesView = list[0]
            countFillObject ++
            this.observableFillObject.onNext(countFillObject)
        }
    }

    private fun resultCreateSecurityOptions(value: Boolean?){
        println("CREATE ALARM: ${value.toString()}")
    }

    private fun resultSecurityOptions(list: List<SecurityOptionsView>?){
        if (!list.isNullOrEmpty()){
            if (!Variables.securityOptionList.isEmpty())
                Variables.securityOptionList.clear()
            for (sov: SecurityOptionsView in list){
                Variables.securityOptionList.add(sov)
            }

            if (Variables.securityOptionList.size == Constants.securityOptions.count()){
                Variables.securityButtonViewSelected = Variables.securityOptionList[defaultButton]
                (activity!!.application as App).navigator.showMain(activity!!)
                (activity!! as RegisterActivity).finish()

            }
            println("SECURITY OPTIONS LOAD NUMBER : ${Variables.securityOptionList.size}")

        }
    }

    private fun resultCreateAlarm(value: Boolean?){
        println("CREATE ALARM: ${value.toString()}")
    }

    private fun resultCreateDevice(value: Boolean?){
        if (value != null && value){
            for (opt:String in Constants.securityOptions){
                createSecurityOptionsData(opt)
            }
            println("CREATE DEVICES: $value")
        }
    }


    private fun handleGetValidate(value: BodyView?){
        et_number.setText("")
        hideProgress()
        if (value != null){
            if (value.error){
                context!!.toast("Error: ${value.error}\nMessage: ${value.message}")

            }else{
                if (value.cra != null){
                    createAlarmCenterData(value.cra)
                    if (value.device != null)
                        createDeviceData(value.device)
                    val prefs = PreferenceRepository.customPrefs(activity!!,
                        Constants.preference_secursos)
                    prefs[Constants.appInit] = "1"

                }else{
                    context!!.toast(getString(R.string.lbl_entity_empty))
                }
            }
        }
    }

    private fun createSecurityOptionsData(prefix: String){
        GlobalScope.launch {
            val securityView = Conversion
                .matchedSecurityOptionsView(prefix)
            createSecurityOptionsDataViewModel.securityView = securityView
            createSecurityOptionsDataViewModel.createSecurityOptionsData()
        }
    }

    private fun createAlarmCenterData(cra: String){
        GlobalScope.launch {
            val acv = Conversion.matchedAlarmCenterView(cra)
            if (acv != null){
                createAlarmCenterDataViewModel.acv = acv
                createAlarmCenterDataViewModel.createAlarmCenterData()

            }

        }
    }

    private fun createDeviceData(device: String){
        GlobalScope.launch {
            val dev = Conversion.matchedDeviceView(device)
            if (dev != null){
                createDeviceDataViewModel.device = dev
                createDeviceDataViewModel.createDeviceData()

            }

        }
    }


    private fun verifyNumber(){
        val url = getUrl()
        if (url != null){
            showProgress()
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

            val url = String.format("${Constants.urlBase}${Constants.deviceServiceNumber}" +
                    "$servicesNumber/$deviceId")
            url

        }else{
            null
        }

    }

    override fun renderFailure(message: Int) {
        hideProgress()
        notify(message)
        /*(activity!!.application as App).navigator.showMain(activity!!)
        (activity!! as RegisterActivity).finish()*/
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}