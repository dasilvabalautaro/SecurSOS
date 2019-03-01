package es.securcom.securso.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.securso.R
import es.securcom.securso.extension.failure
import es.securcom.securso.extension.observe
import es.securcom.securso.extension.viewModel
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.model.persistent.files.ManageFiles
import es.securcom.securso.model.persistent.network.entity.PendingShipping
import es.securcom.securso.presentation.plataform.BaseFragment
import es.securcom.securso.presentation.tools.Conversion
import kotlinx.android.synthetic.main.view_message.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageFragment: BaseFragment() {

    @Inject
    lateinit var manageFiles: ManageFiles

    override fun layoutId() = R.layout.view_message
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        postRepositoryViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendPost)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_send_message.setOnClickListener { sendMessage() }
        eventClickButtonOptionSecurity()
        enabledOptions()
        loadIcons(manageFiles.sizeImageMin, manageFiles.sizeImageMin)
    }

    private fun launchMessage(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageSendMessage(et_message.text.toString())
                val list = listOf(uri, body)
                val pendingShipping = PendingShipping(uri, body, 0)
                Variables.pendingList.add(pendingShipping)
                postRepositoryViewModel.params = list
                postRepositoryViewModel.post()
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun sendMessage(){
        if(et_message.text.isNotEmpty()){
            val url = Conversion.getUrlPort()
            if (!url.isNullOrEmpty()){
                launchMessage(url)

            }else{
                context!!.toast(getString(R.string.lbl_url_empty))
            }

        }else{
            context!!.toast(getString(R.string.lbl_entity_empty))
        }

    }

    override fun renderFailure(message: Int) {
        val last = Variables.pendingList.lastIndex
        Variables.pendingList[last].status = 1
        notify(message)
    }


}