package es.securcom.securso.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import es.securcom.securso.R
import es.securcom.securso.presentation.plataform.BaseActivity
import es.securcom.securso.presentation.view.fragment.RegisterFragment


class RegisterActivity: BaseActivity() {

    companion object {

        fun callingIntent(context: Context) = Intent(context,
            RegisterActivity::class.java)

    }

    override fun fragment() = RegisterFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enablePermissions.permissionReadExternal(this)
        enablePermissions.permissionWriteExternal(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_config).isVisible = false
        return true

    }


}