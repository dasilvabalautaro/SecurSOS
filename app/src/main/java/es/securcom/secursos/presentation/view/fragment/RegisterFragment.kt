package es.securcom.secursos.presentation.view.fragment

import android.os.Bundle
import android.view.View
import es.securcom.secursos.R
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.model.services.ManagerLocation
import es.securcom.secursos.presentation.navigation.Navigator
import es.securcom.secursos.presentation.plataform.BaseFragment
import kotlinx.android.synthetic.main.view_register.*
import javax.inject.Inject

class RegisterFragment: BaseFragment() {

    @Inject
    internal lateinit var navigator: Navigator
    @Inject
    lateinit var managerLocation: ManagerLocation

    override fun layoutId() = R.layout.view_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        if (!managerLocation.isJobServiceOn()){
            if (!managerLocation.start()){
                println("Service Location not run.")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       /* btn.setOnClickListener{
            val prefs = PreferenceRepository.customPrefs(activity!!,
                PreferenceRepository.preferenceApp)
            val ltd = prefs.getString(
                PreferenceRepository
                    .latitude, "")

            tv_latitude.text = ltd
        }*/
    }

    override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        managerLocation.cancel()
    }
}