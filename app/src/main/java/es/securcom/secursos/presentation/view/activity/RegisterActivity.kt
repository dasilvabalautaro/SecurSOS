package es.securcom.secursos.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import es.securcom.secursos.App
import es.securcom.secursos.di.ApplicationComponent
import es.securcom.secursos.presentation.permission.EnablePermissions
import es.securcom.secursos.presentation.plataform.BaseActivity
import es.securcom.secursos.presentation.view.fragment.RegisterFragment
import javax.inject.Inject

class RegisterActivity: BaseActivity() {
    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }
    @Inject
    lateinit var enablePermissions: EnablePermissions

    companion object {

        fun callingIntent(context: Context) = Intent(context,
            RegisterActivity::class.java)

    }

    override fun fragment() = RegisterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enablePermissions.permissionServiceLocation(this)
    }

}