package es.securcom.secursos.presentation.view.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import es.securcom.secursos.App
import es.securcom.secursos.R
import es.securcom.secursos.extension.failure
import es.securcom.secursos.extension.observe
import es.securcom.secursos.extension.viewModel
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.model.persistent.hardware.RecorderSound
import es.securcom.secursos.presentation.component.TimerUtils
import es.securcom.secursos.presentation.plataform.BaseFragment
import es.securcom.secursos.presentation.tools.Conversion
import es.securcom.secursos.presentation.view.activity.AlarmActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_alarm.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AlarmFragment: BaseFragment() {

    @Inject
    lateinit var timerUtils: TimerUtils
    @Inject
    lateinit var manageFiles: ManageFiles
    @Inject
    lateinit var recorderSound: RecorderSound

    private var lastSentEvent = ""
    private var pendingSnaps = 0

    override fun layoutId() = R.layout.view_alarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        postRepositoryViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendPostAlarm)
            failure(failure, ::handleFailureAlarm)
        }
        val hearEvent = timerUtils.eventObserver.observableLocation.map { e -> e }
        disposable.add(hearEvent.observeOn(AndroidSchedulers.mainThread())
            .subscribe { e ->
                kotlin.run {
                    when(e){
                        Variables.LastSentEvent.DEAD.event -> {
                            bt_reset.visibility = View.VISIBLE
                            sendPostInitDead()
                        }
                        Variables.LastSentEvent.SEND_ALARM.event -> {
                            sendPostAlarm()
                        }
                        Variables.LastSentEvent.SAVE_RECORDER.event -> {
                            val audio64 = manageFiles
                                .encodeFileAsBase64(manageFiles.getStorageDirectory()
                                    .absolutePath + "/" + recorderSound.nameFile)
                            val fileSize = manageFiles.fileSize
                            val base64Length = audio64.length
                            sendPostAudio(fileSize, base64Length, audio64)

                        }
                    }
                }
            })

        val image = (activity as AlarmActivity).observableImage.map { i -> i }
        disposable.add(image.observeOn(Schedulers.newThread())
            .map { i ->
                kotlin.run {
                    return@map Bitmap.createScaledBitmap(i,
                        (i.width*0.7).toInt(),
                        (i.height*0.7).toInt(), true)
                }
            }
            .subscribe { resize ->
                kotlin.run {
                    val img64 = manageFiles.base641EncodedImage(resize)
                    val fileLength = resize.allocationByteCount.toLong()
                    val base64Length = img64.length
                    sendPostImage(fileLength, base64Length, img64)
                }
            })
        this.pendingSnaps = Variables.securityButtonViewSelected!!.snap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_count.text = (Variables.securityButtonViewSelected!!.count).toString()
        timerUtils.progressBar = pb_countdown
        timerUtils.tvCounter = tv_count
        bt_reset.visibility = View.INVISIBLE
        ib_sos.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    if(Variables.securityButtonViewSelected!!.notificarprealarma == 1 &&
                        Variables.securityButtonViewSelected!!.alarmaretardada == 1){
                        //Send prealarma server
                        sendPostPreAlarm()
                    }
                    timerUtils.startPreAlarmTimer()

                }
                MotionEvent.ACTION_UP -> {
                    timerUtils.verifyActionUp()
                }
                else -> {}
            }
            return@setOnTouchListener true
        }

        bt_reset.setOnClickListener {
            timerUtils.resetTimers()
            tv_count.text = timerUtils.timerLengthSeconds.toString()
            this.pendingSnaps = Variables.securityButtonViewSelected!!.snap
        }

        bt_cancel.setOnClickListener { cancel() }

    }

    override fun onResume() {
        super.onResume()
        timerUtils.initTimerOfDead()
        /*GlobalScope.launch {
            timerUtils.initTimerOfDead()
        }*/
        buildPackageData.line = Variables.alarmCenter!!.line?:""
        buildPackageData.comments = getString(R.string.last_position)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerUtils.destroy()
        disposable.dispose()
    }

    private fun cancel(){
        if(Variables.securityButtonViewSelected!!.alarmaretardada == 1 &&
            timerUtils.timerStatePre == TimerUtils.TimerState.Running){
            sendPostCancel()
        }
        timerUtils.destroy()
        onBackPressed()
        //(activity!!.application as App).navigator.showMain(activity!!)
        //activity!!.finish()
    }

    private fun resultSendPostAlarm(value: String?){
        if (!value.isNullOrEmpty()){
            val last = Variables.pendingList.lastIndex
            Variables.pendingList.removeAt(last)
            context!!.toast(value.toString())

            when(this.lastSentEvent){
                Variables.securityButtonViewSelected!!.evcode,
                Variables.LastSentEvent.IMAGE.event -> {
                    eventImage()
                }
                Variables.LastSentEvent.AUDIO.event -> {
                    timerUtils.resetTimers()
                }
            }
        }

    }

    private fun eventImage(){
        when {
            this.pendingSnaps > 0 -> (activity as AlarmActivity).camera()
            Variables.securityButtonViewSelected!!.audio != 0 -> {
                context!!.toast(getString(R.string.init_recording))
                recorderSound.start((Variables
                    .securityButtonViewSelected!!.audio + 1000).toLong())
                context!!.toast(getString(R.string.recording))
            }
            else -> timerUtils.resetTimers()
        }
    }

    override fun renderFailure(message: Int) {
        notify(message)
    }

    private fun launchAudio(url: String, fileSize: Long, base64Length: Int,
                            string64Media: String){
        GlobalScope.launch {
            val uri = "http://$url"
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageMedia(fileSize, base64Length, string64Media)
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchImage(url: String, fileSize: Long, base64Length: Int,
                            string64Media: String){
        GlobalScope.launch {
            val uri = "http://$url"
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageMedia(fileSize, base64Length, string64Media)
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchCancel(url: String){
        GlobalScope.launch {
            val uri = "http://$url"

            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageGenericDataLine(Variables.LastSentEvent.CANCEL.event)
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchDeadMan(url: String){
        GlobalScope.launch {
            val uri = "http://$url"

            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageGenericDataLine(Variables.LastSentEvent.INIT_DEAD.event)
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchPreAlarm(url: String){
        GlobalScope.launch {
            val uri = "http://$url"

            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackagePreAlarm(Variables
                        .securityButtonViewSelected!!.tiempodefectodemoraminutos)
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun launchAlarm(url: String){
        GlobalScope.launch {
            val uri = "http://$url"

            buildPackageData.code = Variables.securityButtonViewSelected!!.evcode?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageByEvent()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun sendPostAudio(fileSize: Long, base64Length: Int,
                              string64Media: String){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchAudio(url, fileSize, base64Length, string64Media)
            lastSentEvent = Variables.LastSentEvent.AUDIO.event

        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    private fun sendPostImage(fileSize: Long, base64Length: Int,
                              string64Media: String){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchImage(url, fileSize, base64Length, string64Media)
            lastSentEvent = Variables.LastSentEvent.IMAGE.event
            this.pendingSnaps --
        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    private fun sendPostPreAlarm(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchPreAlarm(url)
            lastSentEvent = Variables.LastSentEvent.TIMEOUT.event
        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun sendPostAlarm(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchAlarm(url)
            lastSentEvent = Variables.securityButtonViewSelected!!.evcode?:""
            val log = "Envío de alarma $lastSentEvent a las: " +
                    SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            manageFiles.writeFile(manageFiles.fileLogName, log)

        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    private fun sendPostCancel(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchCancel(url)
            lastSentEvent = Variables.LastSentEvent.CANCEL.event
        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    private fun sendPostInitDead(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchDeadMan(url)
            lastSentEvent = Variables.LastSentEvent.DEAD.event
        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    private fun handleFailureAlarm(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is Failure.DatabaseError -> renderFailure(R.string.failure_database_error)
        }
        //timerUtils.resetTimers()
    }
}