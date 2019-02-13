package es.securcom.secursos.presentation.view.fragment

import android.annotation.SuppressLint
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
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import es.securcom.secursos.presentation.presenter.PostRepositoryViewModel
import es.securcom.secursos.presentation.presenter.UpdateEventualDataViewModel
import es.securcom.secursos.presentation.tools.Conversion
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
    lateinit var manageFiles: ManageFiles

    private lateinit var updateEventualDataViewModel: UpdateEventualDataViewModel

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

       postRepositoryViewModel = viewModel(viewModelFactory) {
           observe(result, ::resultSendPost)
           failure(failure, ::handleFailure)
       }

       val hearLocation = locationChangeObserver.observableLocation.map { l -> l }
       disposable.add(hearLocation .observeOn(AndroidSchedulers.mainThread())
           .subscribe { l ->
               kotlin.run {
                   val lat = l.latitude
                   val long = l.longitude
                   if (Variables.eventualData != null){
                       Variables.eventualData!!.latitude = lat
                       Variables.eventualData!!.longitude = long

                   }
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
        eventClickButtonOptionSecurity()
        enabledOptions()
        loadIcons(manageFiles.sizeImageMin, manageFiles.sizeImageMin)
        bt_test.setOnClickListener{
            if (Variables.devicesView != null &&
                Variables.alarmCenter != null){
                sendTest()
            }else{
                println(getString(R.string.lbl_entity_empty))
            }
        }
    }

    private fun launchTest(url: String){
        GlobalScope.launch {
            val uri = "http://$url"
            buildPackageData.line = Variables.alarmCenter!!.line?:""
            if (buildPackageData.line.isNotEmpty()){
                buildPackageData.code = "TEST"
                val body = buildPackageData
                    .buildPackageByEvent()
                sendPost(uri, body)
            }else{
                activity!!.runOnUiThread {
                    context!!.toast(getString(R.string.lbl_line_empty))
                }
            }
        }

    }

    private fun sendTest(){

        val url = Conversion.getUrlPort()
        if (!url.isNullOrEmpty()){
            launchTest(url)

        }else{
            context!!.toast(getString(R.string.lbl_url_empty))
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
       notify(message)
    }

}