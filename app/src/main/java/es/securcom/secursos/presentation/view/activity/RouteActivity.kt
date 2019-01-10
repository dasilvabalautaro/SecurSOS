package es.securcom.secursos.presentation.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import es.securcom.secursos.App
import es.securcom.secursos.R
import es.securcom.secursos.di.ApplicationComponent
import es.securcom.secursos.model.persistent.network.NetworkHandler
import es.securcom.secursos.presentation.navigation.Navigator
import javax.inject.Inject

class RouteActivity : AppCompatActivity() {
    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    internal lateinit var navigator: Navigator
    @Inject
    lateinit var networkHandler: NetworkHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        if (networkHandler.isConnected == null || !networkHandler.isConnected!!){
            Toast.makeText(this,
                getString(R.string.failure_network_connection),
                Toast.LENGTH_SHORT).show()

        }
        navigator.showScreenInit(this)
    }


}
