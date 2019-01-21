package es.securcom.secursos.presentation.view.fragment

import es.securcom.secursos.R
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.presentation.plataform.BaseFragment
import kotlinx.android.synthetic.main.view_information.*

class InformationFragment: BaseFragment() {

    override fun layoutId() = R.layout.view_information

    override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        tv_information.text = Variables.alarmCenter!!.information
    }
}