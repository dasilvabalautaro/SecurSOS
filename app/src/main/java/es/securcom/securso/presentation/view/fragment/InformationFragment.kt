package es.securcom.securso.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.securso.R
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.model.persistent.files.ManageFiles
import es.securcom.securso.presentation.plataform.BaseFragment
import es.securcom.securso.presentation.tools.Conversion
import kotlinx.android.synthetic.main.view_information.*
import javax.inject.Inject

class InformationFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles

    override fun layoutId() = R.layout.view_information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventClickButtonOptionSecurity()
    }

    override fun onResume() {
        super.onResume()
        enabledOptions()
        loadIcons(manageFiles.sizeImageMin, manageFiles.sizeImageMin)
        if (Variables.alarmCenter != null &&
            Variables.alarmCenter!!.information != null){
            val value = Conversion
                .getValueStringOfJson(Variables.alarmCenter!!.information!!)
            tv_information.text = value
        }

    }

}