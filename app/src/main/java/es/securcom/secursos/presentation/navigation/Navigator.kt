package es.securcom.secursos.presentation.navigation

import android.content.Context
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.presentation.view.activity.ConnectionActivity
import es.securcom.secursos.presentation.view.activity.RegisterActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    fun showScreenInit(context: Context) {
        val prefs = PreferenceRepository.customPrefs(context,
            PreferenceRepository.preferenceApp)
        val isExist = prefs.getBoolean(
            PreferenceRepository
                .isExistReceiverAlarm, false)
        when (isExist) {
            true -> showMain(context)
            false -> showRegister(context)
        }
    }

    private fun showMain(context: Context) = context
        .startActivity(ConnectionActivity.callingIntent(context))

    private fun showRegister(context: Context) = context
        .startActivity(RegisterActivity.callingIntent(context))
}