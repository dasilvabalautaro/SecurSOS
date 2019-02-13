package es.securcom.secursos.presentation.navigation

import android.content.Context
import es.securcom.secursos.model.persistent.caching.Constants
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.presentation.view.activity.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class Navigator {

    fun showConnection(context: Context) = context
        .startActivity(ConnectionActivity.callingIntent(context))

    fun showMain(context: Context) = context
        .startActivity(MainActivity.callingIntent(context))

    fun showTest(context: Context) = context
        .startActivity(TestActivity.callingIntent(context))

    fun showLog(context: Context) = context
        .startActivity(LogActivity.callingIntent(context))

    fun showInformation(context: Context) = context
        .startActivity(InformationActivity.callingIntent(context))

    fun showMessage(context: Context) = context
        .startActivity(MessageActivity.callingIntent(context))

    fun showAlarm(context: Context) = context
        .startActivity(AlarmActivity.callingIntent(context))

    fun showRegister(context: Context) = context
        .startActivity(RegisterActivity.callingIntent(context))

    fun start(context: Context){
        try {
            val prefs = PreferenceRepository.customPrefs(context,
                Constants.preference_secursos)
            val init = prefs.getString(Constants.appInit, "")
            if (init == "1"){
                showMain(context)
            }else{
                showRegister(context)
            }

        }catch (ex: Exception){
            println(ex.message)
            showRegister(context)
        }
    }

}