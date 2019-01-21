package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import es.securcom.secursos.R
import es.securcom.secursos.presentation.plataform.BaseFragment

class AlarmFragment: BaseFragment() {

    override fun layoutId() = R.layout.view_alarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}