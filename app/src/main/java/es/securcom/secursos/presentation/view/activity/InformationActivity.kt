package es.securcom.secursos.presentation.view.activity

import android.content.Context
import android.content.Intent
import es.securcom.secursos.presentation.plataform.BaseActivity
import es.securcom.secursos.presentation.view.fragment.InformationFragment


class InformationActivity: BaseActivity() {

    companion object {

        fun callingIntent(context: Context) = Intent(context,
            InformationActivity::class.java)

    }

    override fun fragment()  = InformationFragment()
}