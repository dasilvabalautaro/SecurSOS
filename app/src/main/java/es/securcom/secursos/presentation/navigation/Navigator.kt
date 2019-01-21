package es.securcom.secursos.presentation.navigation

import android.content.Context
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.presentation.view.activity.*
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

}