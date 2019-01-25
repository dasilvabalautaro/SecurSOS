package es.securcom.secursos.model.persistent.files

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import es.securcom.secursos.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageFiles @Inject constructor(private val context: Context) {
    private val directoryWork = "secursos"
    val fileLogName = "operation.log"
    var statusLog = false

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
}