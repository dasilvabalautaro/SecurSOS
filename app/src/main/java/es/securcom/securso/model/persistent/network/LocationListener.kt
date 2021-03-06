package es.securcom.securso.model.persistent.network

import android.content.Context
import android.location.Location
import android.os.Bundle
import es.securcom.securso.App
import es.securcom.securso.di.ApplicationComponent
import es.securcom.securso.model.observer.LocationChangeObserver
import javax.inject.Inject


class LocationListener (provider: String, private val context: Context):
    android.location.LocationListener {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (context.applicationContext as App).component
    }

    private var lastLocation: Location? = null

    @Inject
    lateinit var locationChangeObserver: LocationChangeObserver

    init {
        appComponent.inject(this)
        lastLocation = Location(provider)
    }

    override fun onLocationChanged(location: Location) {
        println("onLocationChanged: $location")
        lastLocation!!.set(location)

        locationChangeObserver.location = lastLocation
        locationChangeObserver.observableLocation.onNext(locationChangeObserver
            .location!!)

/*
        val lastUpdate = prefs.getLong(
            PreferenceRepository
                .differUpdateLocation, 0)
        val differ =  (location.time - lastUpdate)
        prefs[PreferenceRepository.differUpdateLocation] =
*/


        println("LastLocation: " + lastLocation!!.latitude.toString() +
                "  " + lastLocation!!.longitude.toString())
    }

    override fun onProviderDisabled(provider: String) {
        println("onProviderDisabled: $provider")
    }

    override fun onProviderEnabled(provider: String) {
        println("onProviderEnabled: $provider")
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        println("onStatusChanged: $provider")
    }

}