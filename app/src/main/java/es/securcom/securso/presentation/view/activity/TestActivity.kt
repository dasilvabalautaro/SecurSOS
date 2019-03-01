package es.securcom.securso.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import es.securcom.securso.presentation.plataform.BaseActivity
import es.securcom.securso.presentation.view.fragment.TestFragment

class TestActivity: BaseActivity() {

    companion object {

        fun callingIntent(context: Context) = Intent(context,
            TestActivity::class.java)

    }

    override fun fragment() = TestFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enablePermissions.permissionServiceLocation(this)
    }
}