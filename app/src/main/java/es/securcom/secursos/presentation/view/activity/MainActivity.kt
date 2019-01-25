package es.securcom.secursos.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import es.securcom.secursos.R
import es.securcom.secursos.model.observer.LocationChangeObserver
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.hardware.CheckBattery
import es.securcom.secursos.model.services.JobPendingService
import es.securcom.secursos.model.services.ManagerLocation
import es.securcom.secursos.model.services.ManagerPendingService
import es.securcom.secursos.presentation.plataform.BaseActivity
import es.securcom.secursos.presentation.view.fragment.MainFragment
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : BaseActivity() {
    companion object {

        fun callingIntent(context: Context) = Intent(context,
            MainActivity::class.java)

    }

    @Inject
    lateinit var managerLocation: ManagerLocation
    @Inject
    lateinit var locationChangeObserver: LocationChangeObserver
    @Inject
    lateinit var checkBattery: CheckBattery
    @Inject
    lateinit var managerPendingService: ManagerPendingService

    override fun fragment() = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enablePermissions.permissionServiceLocation(this)
        enablePermissions.permissionReadExternal(this)
        enablePermissions.permissionWriteExternal(this)

        if (networkHandler.isConnected == null || !networkHandler.isConnected!!){
            Toast.makeText(this,
                getString(R.string.failure_network_connection),
                Toast.LENGTH_SHORT).show()

        }
        if (!managerLocation.isJobServiceOn()){
            if (!managerLocation.start()){
                println("Service Location not run.")
            }
        }

        if (!managerPendingService.isJobServiceOn()){
            if (!managerPendingService.start()){
                println("Service Location not run.")
            }
        }

        val hearLocation = locationChangeObserver.observableLocation.map { l -> l }
        disposable.add(hearLocation .observeOn(Schedulers.newThread())
            .subscribe { l ->
                kotlin.run {
                    val lat = l.latitude
                    val long = l.longitude
                    if (Variables.eventualData != null){
                        Variables.eventualData!!.latitude = lat
                        Variables.eventualData!!.longitude = long

                    }

                }
            })
        val hearBattery = checkBattery.batteryStatusReceiver.observableBatteryStatus.map { l->l }
        disposable.add(hearBattery .observeOn(Schedulers.newThread())
            .subscribe { l ->
                kotlin.run {
                    if (Variables.eventualData != null){
                        Variables.eventualData!!.batteryLevel = l
                    }

                }
            })


    }

    override fun onDestroy() {
        super.onDestroy()
        managerLocation.cancel()
        managerPendingService.cancel()
        disposable.dispose()
    }
}
