package es.securcom.secursos.model.persistent.network

import android.content.Context
import android.location.Location
import android.os.Bundle
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.model.persistent.preference.PreferenceRepository.set


class LocationListener (private val provider: String, private val context: Context):
    android.location.LocationListener {

    private var lastLocation: Location? = null


    init {
        lastLocation = Location(provider)
    }

    override fun onLocationChanged(location: Location) {
        println("onLocationChanged: $location")
        lastLocation!!.set(location)
        val prefs = PreferenceRepository.customPrefs(context,
            PreferenceRepository.preferenceApp)
        prefs[PreferenceRepository.latitude] = lastLocation!!.latitude.toString()
        prefs[PreferenceRepository.longitude] = lastLocation!!.longitude.toString()
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