package es.securcom.secursos.presentation.plataform

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import es.securcom.secursos.App
import es.securcom.secursos.R
import kotlinx.android.synthetic.main.toolbar.*
import es.securcom.secursos.R.layout
import es.securcom.secursos.R.id
import es.securcom.secursos.di.ApplicationComponent
import es.securcom.secursos.extension.inTransaction
import es.securcom.secursos.model.persistent.network.NetworkHandler
import es.securcom.secursos.presentation.permission.EnablePermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).component
    }

    @Inject
    lateinit var networkHandler: NetworkHandler
    @Inject
    lateinit var enablePermissions: EnablePermissions
    internal var disposable: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, null,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        addFragment(savedInstanceState)
        bt_connect.setOnClickListener{
            (application as App).navigator.showConnection(this)
        }

        bt_test.setOnClickListener {
            (application as App).navigator.showTest(this)
        }

        bt_log.setOnClickListener {
            (application as App).navigator.showLog(this)
        }

        bt_information.setOnClickListener {
            (application as App).navigator.showInformation(this)
        }

        bt_message.setOnClickListener {
            (application as App).navigator.showMessage(this)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu!!.findItem(R.id.action_config).isVisible = true
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_config){
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction { add(
            id.fragmentContainer, fragment()) }

    abstract fun fragment(): BaseFragment


}