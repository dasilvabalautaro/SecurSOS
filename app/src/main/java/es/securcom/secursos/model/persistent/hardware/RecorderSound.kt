package es.securcom.secursos.model.persistent.hardware

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Handler
import android.os.Message
import es.securcom.secursos.model.observer.EventObserver
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.files.ManageFiles
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean


class RecorderSound @Inject constructor(private val manageFiles: ManageFiles,
                                        val eventObserver: EventObserver) {
    lateinit var audioRecorder: AudioRecord
    private val samplingRateInHz = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSizeFactor = 2
    private val recordingInProgress = AtomicBoolean(false)
    private val bufferSize = AudioRecord.getMinBufferSize(samplingRateInHz,
        channelConfig, audioFormat) * bufferSizeFactor
    val nameFile = "temp.wav"
    lateinit var job: Job


    private fun startRecording(){
        audioRecorder = AudioRecord(MediaRecorder.AudioSource.DEFAULT,
            samplingRateInHz, channelConfig, audioFormat, bufferSize)
        audioRecorder.startRecording()
        recordingInProgress.set(true)
        job = GlobalScope.launch {
            try {
                runRecording()
            }catch (e: IOException) {
                println(e.message)
                job.cancel()
            }

        }

    }

    fun stopRecording(){
        recordingInProgress.set(false)
        audioRecorder.stop()
        audioRecorder.release()
        job.cancel()
    }

    private fun runRecording(){
        val outPutFile = File(manageFiles.getStorageDirectory(), nameFile)

        val buffer = ByteBuffer.allocateDirect(bufferSize)

        try {
            FileOutputStream(outPutFile).use { outStream ->
                while (recordingInProgress.get()) {
                    val result = audioRecorder.read(buffer, bufferSize)
                    if (result < 0) {
                        throw RuntimeException(
                            "Reading of audio buffer failed: " +
                                    getBufferReadFailureReason(
                                        result
                                    )
                        )
                    }
                    outStream.write(buffer.array(), 0, bufferSize)
                    buffer.clear()
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Writing of recorded audio failed", e)
        }
    }

    private fun getBufferReadFailureReason(errorCode: Int): String {
        return when (errorCode) {
            AudioRecord.ERROR_INVALID_OPERATION -> "ERROR_INVALID_OPERATION"
            AudioRecord.ERROR_BAD_VALUE -> "ERROR_BAD_VALUE"
            AudioRecord.ERROR_DEAD_OBJECT -> "ERROR_DEAD_OBJECT"
            AudioRecord.ERROR -> "ERROR"
            else -> "Unknown ($errorCode)"
        }
    }

    @SuppressLint("HandlerLeak")
    private fun stopRecordingHandler() = object : Handler() {
        override fun handleMessage(msg: Message) {
            stopRecording()
            eventObserver.value = Variables.LastSentEvent.SAVE_RECORDER.event
            eventObserver.observableLocation.onNext(eventObserver.value!!)
        }
    }

    fun start(delay: Long){
        startRecording()
        stopRecordingHandler().sendEmptyMessageDelayed(0, delay)
    }
}