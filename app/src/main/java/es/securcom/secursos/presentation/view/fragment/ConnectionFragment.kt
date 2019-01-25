package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.secursos.R
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.presentation.plataform.BaseFragment
import kotlinx.android.synthetic.main.view_connection.*

class ConnectionFragment: BaseFragment() {

    override fun layoutId() = R.layout.view_connection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_sync_up.setOnClickListener { setControls() }
    }

    private fun setControls(){
        if (Variables.alarmCenter != null){
            tv_name.text = Variables.alarmCenter!!.name
            tv_phone.text = Variables.alarmCenter!!.phone
            tv_email.text = Variables.alarmCenter!!.email
            tv_web.text = Variables.alarmCenter!!.web

            if (Variables.alarmCenter!!.lapseLocation > 0){
                sw_location.setImageResource(R.drawable.switchon)
            }else{
                sw_location.setImageResource(R.drawable.switchoff)
            }

        }
    }

    override fun renderFailure(message: Int) {
        notify(message)
    }




}