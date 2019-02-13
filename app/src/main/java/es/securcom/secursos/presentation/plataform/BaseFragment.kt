package es.securcom.secursos.presentation.plataform

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import es.securcom.secursos.App
import es.securcom.secursos.R
import es.securcom.secursos.R.color
import es.securcom.secursos.di.ApplicationComponent
import es.securcom.secursos.extension.appContext
import es.securcom.secursos.extension.viewContainer
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.BuildPackageData
import es.securcom.secursos.model.persistent.caching.Constants
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import es.securcom.secursos.presentation.presenter.PostRepositoryViewModel
import es.securcom.secursos.presentation.tools.Conversion
import es.securcom.secursos.presentation.view.fragment.AlarmFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.view_grid.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment: Fragment() {
    companion object {
        var flagInit = true
        var countFillObject = 0
        var lowBattery = false
        var okBattery = false
        var alarmBattery = false
    }

    abstract fun layoutId(): Int
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var buildPackageData: BuildPackageData
    private val optionOne = 0
    private val optionTwo = 1
    private val optionThree = 2
    private val optionFour = 3
    protected var observableFillObject: Subject<Int> = PublishSubject.create()
    protected lateinit var postRepositoryViewModel: PostRepositoryViewModel

    internal var disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    open fun onBackPressed() {
        if (this is AlarmFragment){
            activity!!.finish()
        }

    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun notify(@StringRes message: Int) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int) {
        val snackBar = Snackbar.make(viewContainer, message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setActionTextColor(
            ContextCompat.getColor(appContext,
                color.colorTextPrimary))
        snackBar.show()
    }
    abstract fun renderFailure(@StringRes message: Int)

    internal fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is Failure.DatabaseError -> renderFailure(R.string.failure_database_error)
        }
    }

    protected fun sendPost(uri: String, body: String){
        val list = listOf(uri, body)
        val pendingShipping = PendingShipping(uri, body, 0)
        Variables.pendingList.add(pendingShipping)
        postRepositoryViewModel.params = list
        postRepositoryViewModel.post()
    }

    protected fun resultSendPost(value: String?){
        if (!value.isNullOrEmpty()){
            val last = Variables.pendingList.lastIndex
            Variables.pendingList.removeAt(last)
            context!!.toast(value.toString())
        }

    }

    private fun selectOption(opt: Int){
        if (!Variables.securityOptionList.isEmpty()){
            Variables.securityButtonViewSelected = Variables.securityOptionList[opt]
            (activity!!.application as App).navigator.showAlarm(activity!!)

        }
    }

    private fun launchInitApp(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                val body = buildPackageData
                    .buildPackageApplicationInit()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    protected fun sendPostInit(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){

            launchInitApp(url)

        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
        }

    }

    protected fun enabledOptions(){
        if (Variables.devicesView != null){
            ib_health.visibility = if(Variables.devicesView!!.button1 == optionTwo)
                View.VISIBLE else View.INVISIBLE
            ib_policies.visibility = if(Variables.devicesView!!.button2 == optionTwo)
                View.VISIBLE else View.INVISIBLE
            ib_fireguard.visibility = if(Variables.devicesView!!.button3 == optionTwo)
                View.VISIBLE else View.INVISIBLE
            ib_four.visibility = if(Variables.devicesView!!.button4 == optionTwo)
                View.VISIBLE else View.INVISIBLE
        }

    }

    protected fun eventClickButtonOptionSecurity(){
        ib_health.setOnClickListener { selectOption(optionOne) }
        ib_policies.setOnClickListener { selectOption(optionTwo) }
        ib_fireguard.setOnClickListener { selectOption(optionThree) }
        ib_four.setOnClickListener { selectOption(optionFour) }
    }

    protected fun loadIcons(width: Int, height: Int){
        for (i in 0 until Variables.securityOptionList.size){
            when(i){
                0 -> {
                    Picasso.get()
                        .load(Constants.urlImages + Variables.securityOptionList[i].icon)
                        .placeholder(R.drawable.ic_sync)
                        .error(R.drawable.ic_error)
                        .resize(width, height)
                        .into(ib_health)
                }
                1 -> {
                    Picasso.get()
                        .load(Constants.urlImages + Variables.securityOptionList[i].icon)
                        .placeholder(R.drawable.ic_sync)
                        .error(R.drawable.ic_error)
                        .resize(width, height)
                        .into(ib_policies)
                }
                2 -> {
                    Picasso.get()
                        .load(Constants.urlImages + Variables.securityOptionList[i].icon)
                        .placeholder(R.drawable.ic_sync)
                        .error(R.drawable.ic_error)
                        .resize(width, height)
                        .into(ib_fireguard)
                }
                3 -> {
                    Picasso.get()
                        .load(Constants.urlImages + Variables.securityOptionList[i].icon)
                        .placeholder(R.drawable.ic_sync)
                        .error(R.drawable.ic_error)
                        .resize(width, height)
                        .into(ib_four)
                }
            }
        }

    }


    internal fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}