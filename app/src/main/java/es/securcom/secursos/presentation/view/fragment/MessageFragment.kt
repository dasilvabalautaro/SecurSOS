package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.secursos.R
import es.securcom.secursos.extension.failure
import es.securcom.secursos.extension.observe
import es.securcom.secursos.extension.viewModel
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import es.securcom.secursos.presentation.plataform.BaseFragment
import es.securcom.secursos.presentation.presenter.PostRepositoryViewModel
import es.securcom.secursos.presentation.tools.Conversion
import kotlinx.android.synthetic.main.view_message.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageFragment: BaseFragment() {


    private lateinit var postRepositoryViewModel: PostRepositoryViewModel

    override fun layoutId() = R.layout.view_message
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        postRepositoryViewModel = viewModel(viewModelFactory) {
            observe(result, ::resultSendMessage)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_send_message.setOnClickListener { sendMessage() }
    }

    private fun launchMessage(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageSendMessage(et_message.text.toString())
                val list = listOf(uri, body)
                val pendingShipping = PendingShipping(url, body, 0)
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

    private fun resultSendMessage(value: String?){
        if (value.isNullOrEmpty()){
            val last = Variables.pendingList.lastIndex
            Variables.pendingList.removeAt(last)
            context!!.toast(value.toString())
        }

    }

    override fun renderFailure(message: Int) {
        val last = Variables.pendingList.lastIndex
        Variables.pendingList[last].status = 1
        notify(message)
    }


}