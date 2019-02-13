package es.securcom.secursos.presentation.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.FileProvider
import es.securcom.secursos.model.persistent.files.ManageFiles
import es.securcom.secursos.presentation.plataform.BaseActivity
import es.securcom.secursos.presentation.view.fragment.AlarmFragment
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class AlarmActivity: BaseActivity() {


    companion object {

        fun callingIntent(context: Context) = Intent(context,
            AlarmActivity::class.java)

    }

    @Inject
    lateinit var manageFiles: ManageFiles

    var image: Bitmap? = null
    var observableImage: Subject<Bitmap> = PublishSubject.create()

    override fun fragment() = AlarmFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        enablePermissions.permissionCamera(this)
        enablePermissions.permissionReadExternal(this)
        enablePermissions.permissionWriteExternal(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == enablePermissions.accessCamera &&
            resultCode == Activity.RESULT_OK) {

            val photoUri = FileProvider.getUriForFile(this,
                applicationContext.packageName +
                        ".provider", manageFiles
                    .getCameraFile())
            this.image = manageFiles.getBitmap(photoUri)
            if (this.image != null){
                this.observableImage.onNext(this.image!!)
            }

        }
    }

    fun camera(){
        enablePermissions.startCamera(this)
    }
}