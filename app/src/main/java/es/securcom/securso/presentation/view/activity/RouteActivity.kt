package es.securcom.securso.presentation.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.securcom.securso.App

class RouteActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).navigator.start(this)
    }
}