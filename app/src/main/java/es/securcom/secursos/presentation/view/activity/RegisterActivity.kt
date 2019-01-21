package es.securcom.secursos.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.view.Menu
import es.securcom.secursos.R
import es.securcom.secursos.presentation.plataform.BaseActivity
import es.securcom.secursos.presentation.view.fragment.RegisterFragment


class RegisterActivity: BaseActivity() {

    companion object {

        fun callingIntent(context: Context) = Intent(context,
            RegisterActivity::class.java)

    }

    override fun fragment() = RegisterFragment()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_config).isVisible = false
        return true

    }


}