package es.securcom.secursos.presentation.view.fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.securcom.secursos.R
import es.securcom.secursos.model.services.ManagerLocation
import es.securcom.secursos.presentation.plataform.BaseFragment
import javax.inject.Inject
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.view_test.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import es.securcom.secursos.extension.failure
import es.securcom.secursos.extension.observe
import es.securcom.secursos.extension.viewModel
import es.securcom.secursos.model.observer.LocationChangeObserver
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.preference.PreferenceRepository
import es.securcom.secursos.presentation.presenter.UpdateEventualDataViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TestFragment: BaseFragment(), OnMapReadyCallback{

    private lateinit var googleMaps: GoogleMap

    @Inject
    lateinit var managerLocation: ManagerLocation
    @Inject
    lateinit var locationChangeObserver: LocationChangeObserver
    @Inject
    lateinit var updateEventualDataViewModel: UpdateEventualDataViewModel


    override fun layoutId() = R.layout.view_test

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
       if (!managerLocation.isJobServiceOn()){
            if (!managerLocation.start()){
                println("Service Location not run.")
            }
        }
       updateEventualDataViewModel = viewModel(viewModelFactory) {
           observe(result, ::resultUpdateEventual)
           failure(failure, ::handleFailure)
       }
       val hearLocation = locationChangeObserver.observableLocation.map { l -> l }
       disposable.add(hearLocation .observeOn(AndroidSchedulers.mainThread())
           .subscribe { l ->
               kotlin.run {
                   val lat = l.latitude
                   val long = l.longitude
                   Variables.eventualData!!.latitude = lat
                   Variables.eventualData!!.longitude = long
                   updateEventualLocation()
                   val coordinates = LatLng(lat, long)
                   googleMaps.addMarker(MarkerOptions().position(coordinates)
                       .title(getString(R.string.title_my_location)))
                   googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))

               }
           })

    }

    private fun updateEventualLocation(){
        GlobalScope.launch {
            updateEventualDataViewModel.eventual = Variables.eventualData
            updateEventualDataViewModel.updateEventualData()

        }
    }

    private fun resultUpdateEventual(value: Boolean?){
        println(value.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = childFragmentManager
            .findFragmentById(R.id.map_view) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        bt_test.setOnClickListener{

        }
    }



    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMaps = googleMap!!
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        if (Variables.eventualData != null){
            val lat = Variables.eventualData!!.latitude
            val lng = Variables.eventualData!!.longitude
            val coordinates = LatLng(lat, lng)
            googleMaps.addMarker(MarkerOptions().position(coordinates)
                .title(getString(R.string.title_my_location)))
            googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))

        }

    }



    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
     }

   override fun renderFailure(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}