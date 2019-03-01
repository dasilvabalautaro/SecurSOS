package es.securcom.securso.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.securso.R
import es.securcom.securso.model.persistent.files.ManageFiles
import es.securcom.securso.presentation.plataform.BaseFragment
import kotlinx.android.synthetic.main.view_log.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles

    override fun layoutId() = R.layout.view_log

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventClickButtonOptionSecurity()
        loadFileLog()
        enabledOptions()
        loadIcons(manageFiles.sizeImageMin, manageFiles.sizeImageMin)
    }

    private fun loadFileLog(){
        GlobalScope.launch {
            val stringLog = manageFiles.readFromFile(manageFiles.fileLogName)
            activity!!.runOnUiThread {
                tv_log.text = stringLog
            }

        }
    }

    override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}