package es.securcom.securso.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.securso.R
import es.securcom.securso.extension.failure
import es.securcom.securso.extension.observe
import es.securcom.securso.extension.viewModel
import es.securcom.securso.model.persistent.caching.Constants
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.model.persistent.files.ManageFiles
import es.securcom.securso.presentation.data.*
import es.securcom.securso.presentation.plataform.BaseFragment
import es.securcom.securso.presentation.presenter.*
import es.securcom.securso.presentation.tools.Conversion
import es.securcom.securso.presentation.view.activity.MainActivity
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_grid.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
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
    private lateinit var createSecurityOptionsDataViewModel: CreateSecurityOptionsDataViewModel
    private lateinit var getSecurityOptionsViewModel: GetSecurityOptionsViewModel
    private val defaultButton = 0

    override fun layoutId() = R.layout.view_grid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        val countFillCreate = observableFillCreate.map { c -> c }
        disposable.add(countFillCreate.observeOn(Schedulers.newThread())
            .subscribe {
                if (it == 2){
                    for (opt:String in Constants.securityOptions){
                        createSecurityOptionsData(opt)
                    }
                }
            })

        val countFill = observableFillObject.map { c -> c }
        disposable.add(countFill.observeOn(Schedulers.newThread())
            .subscribe {
                if (it == 2){
                    sendPostInit()
                    val log = "Entrando: ${Variables.devicesView!!.serviceNumber} ID: " +
                            "${Variables.devicesView!!.identifier}"
                    manageFiles.writeFile(manageFiles.fileLogName, log)
                }
            })
        val levelBattery = (activity as MainActivity).observableBattery.map { l -> l }
        disposable.add(levelBattery.observeOn(Schedulers.newThread())
            .subscribe{
                checkBatteryLevel(it)
            })

        if (flagInit){
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

            syncUpViewModel = viewModel(viewModelFactory) {
                observe(result, ::handleSyncUp)
                failure(failure, ::handleFailure)
            }
            createAlarmCenterDataViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultCreateAlarm)
                failure(failure, ::handleFailure)
            }

            createDeviceDataViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultCreateDevices)
                failure(failure, ::handleFailure)
            }

            createSecurityOptionsDataViewModel = viewModel(viewModelFactory) {
                observe(result, ::resultCreateSecurityOptions)
                failure(failure, ::handleFailure)
            }

        }

        postRepositoryViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendPost)
            failure(failure, ::handleFailure)
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

        if (!flagInit){
            loadIcons(manageFiles.sizeImageMax, manageFiles.sizeImageMax)
        }
        eventClickButtonOptionSecurity()

    }

    private fun resultCreateEventual(value: Boolean?){
        println(value.toString())
    }

    private fun resultDevices(list: List<DeviceView>?){
        if (!list.isNullOrEmpty()){
            Variables.devicesView = list[0]

            if (flagInit){
                syncUp()
                flagInit = false
            }

            enabledOptions()
            countFillObject ++
            this.observableFillObject.onNext(countFillObject)
        }
    }

    private fun resultSecurityOptions(list: List<SecurityOptionsView>?){
        if (!list.isNullOrEmpty()){
            if (!Variables.securityOptionList.isEmpty())
                Variables.securityOptionList.clear()
            for (sov: SecurityOptionsView in list){
                Variables.securityOptionList.add(sov)
            }

            loadIcons(manageFiles.sizeImageMax, manageFiles.sizeImageMax)
            setTextButton()
            Variables.securityButtonViewSelected = Variables.securityOptionList[defaultButton]

            println("SECURITY OPTIONS LOAD ${Variables.securityOptionList.size}")
        }
    }

    private fun resultCreateAlarm(value: Boolean?){
        println(value.toString())
        countFillCreate ++
        this.observableFillCreate.onNext(countFillCreate)
    }

    private fun resultCreateDevices(value: Boolean?){
        if (value != null && value){

            println(value.toString())
        }

    }

    private fun resultCreateSecurityOptions(value: Boolean?){
        println(value.toString())
    }


    private fun createSecurityOptionsData(prefix: String){
        GlobalScope.launch {
            val securityView = Conversion
                .matchedSecurityOptionsView(prefix)
            createSecurityOptionsDataViewModel.securityView = securityView
            createSecurityOptionsDataViewModel.createSecurityOptionsData()
        }
    }

    private fun resultAlarmCenter(list: List<AlarmCenterView>?){
        if (!list.isNullOrEmpty()){
            Variables.alarmCenter = list[0]
            countFillObject ++
            this.observableFillObject.onNext(countFillObject)
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
                if (value.cra != null){
                    createAlarmCenterData(value.cra)
                    if (value.device != null)
                        createDeviceData(value.device)

                }else{
                    context!!.toast(getString(R.string.lbl_entity_empty))
                }
            }
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

    private fun syncUp(){
        if(Variables.devicesView != null){
            val url = Conversion.getUrlIdentifier()
            syncUpViewModel.url = url
            syncUpViewModel.syncUp()

        }
    }

    override fun renderFailure(message: Int) {
        notify(message)
    }

    private fun launchStopApp(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageApplicationShutDown()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchLowBattery(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageLowBattery()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchOkBattery(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageBatteryOK()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchAlarmBattery(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageLowBatteryAlarm()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun sendPostStop(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){

            launchStopApp(url)

        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }
    private fun sendPostLowBattery(){
        if (!lowBattery){
            val url = Conversion.getUrlPort()
            if (!url.isNullOrEmpty()){
                launchLowBattery(url)
                lowBattery = true

            }else{
                context!!.toast(getString(R.string.lbl_url_empty))
            }

        }

    }

    private fun sendPostOkBattery(){
        if (!okBattery){
            val url = Conversion.getUrlPort()
            if (!url.isNullOrEmpty()){
                launchOkBattery(url)
                okBattery = true

            }else{
                context!!.toast(getString(R.string.lbl_url_empty))
            }

        }

    }

    private fun sendPostAlarmBattery(){
        if (!alarmBattery){
            val url = Conversion.getUrlPort()
            if (!url.isNullOrEmpty()){
                launchAlarmBattery(url)
                alarmBattery = true

            }else{
                context!!.toast(getString(R.string.lbl_url_empty))
            }

        }

    }

    private fun checkBatteryLevel(level: Int){
        if (level < Variables.alarmCenter!!.lowBatteryAlertValue){
            if (Variables.alarmCenter!!.lowBatteryAlert == 1){
                sendPostLowBattery()
            }
        }else if (level >= Variables.alarmCenter!!.lowBatteryAlertValue + 10){
            sendPostOkBattery()
        }

        if (level < Variables.alarmCenter!!.lowBatteryAlarmValue){
            if (Variables.alarmCenter!!.lowBatteryAlarm == 1){
                sendPostAlarmBattery()
            }
        }
    }

    private fun setTextButton(){
        val language = Locale.getDefault().language.toUpperCase()

        for (i in 0 until Variables.securityOptionList.size){
            when(i){
                0 -> {
                    tv_title_health.text = Conversion.getLabelButton(language,
                    Variables.securityOptionList[i].descr?:"")
                }
                1 -> {
                    tv_title_policies.text = Conversion.getLabelButton(language,
                        Variables.securityOptionList[i].descr?:"")

                }
                2 -> {
                    tv_title_fire.text = Conversion.getLabelButton(language,
                        Variables.securityOptionList[i].descr?:"")

                }
                3 -> {
                    tv_title_four.text = Conversion.getLabelButton(language,
                        Variables.securityOptionList[i].descr?:"")

                }
            }
        }

    }

    override fun onDestroy() {
        sendPostStop()
        super.onDestroy()
        disposable.dispose()
    }

}