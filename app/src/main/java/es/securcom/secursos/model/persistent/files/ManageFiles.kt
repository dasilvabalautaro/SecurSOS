package es.securcom.secursos.model.persistent.files

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.DisplayMetrics
import es.securcom.secursos.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.windowManager
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import org.apache.commons.io.FileUtils


@Singleton
class ManageFiles @Inject constructor(private val context: Context) {
    private val directoryWork = "secursos"
    val fileLogName = "operation.log"
    var statusLog = false
    private val imageFile = "temp.jpg"
    private var screenWidth = 0
    private var screenHeight = 0
    private val displayMetrics = DisplayMetrics()
    private val quality = 100
    private val relationMaxImage = 112
    private val relationMinImage = 70
    var sizeImageMax = 0
    var sizeImageMin = 0
    var fileSize: Long = 0

    init {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        this.screenWidth = displayMetrics.widthPixels
        this.screenHeight = displayMetrics.heightPixels
        GlobalScope.launch {
            getMaxImage()
            getMinImage()
        }
    }

    private fun getMaxImage(){
        val resizeImage = (displayMetrics.density * relationMaxImage).toInt()
        sizeImageMax = resizeImage

        println("SIZE SCREEN: $sizeImageMax  SCREEN: ${displayMetrics.density}")
    }

    private fun getMinImage(){
        val resizeImage = (displayMetrics.density * relationMinImage).toInt()
        sizeImageMin = resizeImage

    }

    fun getStorageDirectory(): File {
        val file = File(Environment.getExternalStorageDirectory(), directoryWork)
        if (!file.exists()) {
            file.mkdirs()
            println("Directory not created")
        }
        return file
    }

    fun writeFile(fileName: String, data: String){
        GlobalScope.launch {
            try {
                val file = File(getStorageDirectory(), fileName)
                if (!file.exists()){
                    file.createNewFile()
                }
                val out = BufferedWriter(FileWriter(file, true) as Writer?, 1024)
                out.write(data)
                out.newLine()
                out.close()

            }catch (io: IOException){
                println(io.message)
            }
        }
    }

    fun readFromFile(fileName: String): String? {

        try {
            val file = File(getStorageDirectory(), fileName)
            val bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            var lineTxt = bufferedReader.readLine()
            while (!lineTxt.isNullOrEmpty()) {
                stringBuilder.appendln(lineTxt)
                lineTxt = bufferedReader.readLine()
            }

            return stringBuilder.toString()

        } catch (e: FileNotFoundException) {
            println("File not found: $e")
        } catch (e: IOException) {
            println("Can not read file: $e")
        }
        return null

    }

    fun delete(fileName: String){
        try {
            val file = File(getStorageDirectory(), fileName)
            file.delete()
        } catch (e: FileNotFoundException) {
            println("File not found: $e")
        } catch (e: IOException) {
            println("Can not read file: $e")
        }

    }

    fun verifyLog(){
        if (!statusLog){
            statusLog = true
            delete(fileLogName)
            initFileLog()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun initFileLog(){
        val cpu = "${context.getString(R.string.lbl_cpu)} ${Build.SUPPORTED_ABIS[0]}"
        val model = "${context.getString(R.string.lbl_model)} ${Build.MODEL}"
        val manufacturer = "${context.getString(R.string.lbl_manufacturer)} ${Build.MANUFACTURER}"
        val brand = "${context.getString(R.string.lbl_brand)} ${Build.BRAND}"
        val androidVersion = "${context.getString(R.string.lbl_android)} ${Build.VERSION.RELEASE}"
        val firstLog = context.getString(R.string.device_first_log) + " " +
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        writeFile(this.fileLogName, firstLog)
        writeFile(this.fileLogName, cpu)
        writeFile(this.fileLogName, model)
        writeFile(this.fileLogName, manufacturer)
        writeFile(this.fileLogName, brand)
        writeFile(this.fileLogName, androidVersion)

    }

    fun getCameraFile(): File {
        return File(getStorageDirectory(), imageFile)
    }

    fun getBitmap(uri: Uri?): Bitmap? {
        return when {
            uri != null -> try {
                MediaStore.Images.Media
                    .getBitmap(context.contentResolver, uri)

            } catch (e: IOException) {
                println(e.message)
                null
            }
            else -> {
                println(R.string.value_null)
                null
            }

        }
    }

    fun base641EncodedImage(bitmap: Bitmap): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }



    fun encodeFileAsBase64(path: String): String{
        try {
            val file = File(path)
            this.fileSize = file.length()
            val fileBytes = FileUtils.readFileToByteArray(file)
            return Base64.encodeToString(fileBytes, Base64.NO_WRAP)
        } catch (e: FileNotFoundException) {
            println("File not found: $e")
        } catch (e: IOException) {
            println("Can not read file: $e")
        }

        return ""
    }

    private fun scaleBitmapDown(bitmap: Bitmap): Bitmap {
        return if (this.screenWidth < bitmap.width){
            val diffRelationSize = this.screenWidth.toFloat() / bitmap.width.toFloat()
            Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * diffRelationSize).toInt(),
                (bitmap.height * diffRelationSize).toInt(), true)
        }else{
            bitmap
        }

    }
}