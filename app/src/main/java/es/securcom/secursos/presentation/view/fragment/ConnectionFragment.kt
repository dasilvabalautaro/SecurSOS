package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.secursos.R
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.presentation.data.SecurityOptionsView
import es.securcom.secursos.presentation.plataform.BaseFragment
import kotlinx.android.synthetic.main.view_connection.*
import javax.inject.Inject

class ConnectionFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles

    override fun layoutId() = R.layout.view_connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sw_audio.setImageResource(R.drawable.switchoff)
        sw_capture.setImageResource(R.drawable.switchoff)
        sw_location.setImageResource(R.drawable.switchoff)
        bt_sync_up.setOnClickListener { setControls() }
        eventClickButtonOptionSecurity()
    }

    override fun onResume() {
        super.onResume()
        setControls()
        enabledOptions()
        loadIcons(manageFiles.sizeImageMin, manageFiles.sizeImageMin)
    }

    private fun setControls(){
        if (Variables.alarmCenter != null){
            tv_name.text = Variables.alarmCenter!!.name
            tv_phone.text = Variables.alarmCenter!!.phone
            tv_email.text = Variables.alarmCenter!!.email
            tv_web.text = Variables.alarmCenter!!.web
            setCheck()

        }
    }

    private fun setCheck(){
        if (Variables.alarmCenter!!.lapseLocation > 0){
            sw_location.setImageResource(R.drawable.switchon)
        }else{
            sw_location.setImageResource(R.drawable.switchoff)
        }

        if (!Variables.securityOptionList.isEmpty()){
            for (scv: SecurityOptionsView in Variables.securityOptionList){
                if (scv.audio > 0){
                    sw_audio.setImageResource(R.drawable.switchon)
                }
                if (scv.snap > 0){
                    sw_capture.setImageResource(R.drawable.switchon)
                }
            }
        }

    }

    override fun renderFailure(message: Int) {
        notify(message)
    }




}