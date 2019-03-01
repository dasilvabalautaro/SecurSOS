package es.securcom.securso.presentation.component

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView
import es.securcom.securso.model.observer.EventObserver
import es.securcom.securso.model.persistent.caching.Variables
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TimerUtils @Inject constructor(val eventObserver: EventObserver) {

    var progressBar: MaterialProgressBar? = null
    var tvCounter:  TextView? = null

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private lateinit var deadTimer: CountDownTimer

    var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    var timerStatePre = TimerState.Stopped
    private var timerStateDead = TimerState.Stopped
    private var secondsRemaining: Long = 0
    private var secondsRemainingDead: Long = 0
    private var timerLengthSecondsDead: Long = 0

    fun startPreAlarmTimer(){
        timerLengthSeconds = lengthPreTimer() //20000
        onSetControls()
        timerStatePre = TimerState.Running
        timer = object : CountDownTimer(timerLengthSeconds, 1000) {
            override fun onFinish() = onTimerFinishedPre()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun startAlarmTimer(){
        timerLengthSeconds = lengthAlarmTimer() //10000
        onSetControls()
        timerState = TimerState.Running
        if (secondsRemainingDead > 0){
            deadTimer.cancel()
            secondsRemainingDead = 0
        }

        timer = object : CountDownTimer(timerLengthSeconds, 1000) {
            override fun onFinish() = onTimerFinishedAlarm()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun startDeadTimer(){
        timerLengthSecondsDead = lengthDeadTimer()
        timerStateDead = TimerState.Running
        deadTimer = object : CountDownTimer(timerLengthSecondsDead, 1000) {
            override fun onFinish() = onTimerFinishedDead()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemainingDead = millisUntilFinished/1000

            }
        }.start()
    }

    fun verifyActionUp(){
        if(secondsRemaining > 0){
            onTimerFinished()
        }
    }

    fun onTimerFinishedDead(){
        timerStateDead = TimerState.Stopped
        secondsRemainingDead = 0
        deadTimer.cancel()
        startAlarmTimer()

    }

    fun onTimerFinishedPre(){
        timerStatePre = TimerState.Stopped
        onTimerFinished()
        startAlarmTimer()

    }

    fun  onTimerFinishedAlarm(){
        timerState = TimerState.Stopped
        onTimerFinished()
        //Send data
        eventObserver.value = Variables.LastSentEvent.SEND_ALARM.event
        eventObserver.observableLocation.onNext(eventObserver.value!!)

    }

    private fun onTimerFinished(){

        secondsRemaining = 0

        updateCountdownUI()

        timer.cancel()

    }

    private fun onSetControls(){
        progressBar!!.progress = 0
        progressBar!!.max = timerLengthSeconds.toInt()

    }

    @SuppressLint("SetTextI18n")
    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        tvCounter!!.text = "$minutesUntilFinished:${if (secondsStr.length == 2)
            secondsStr else "0$secondsStr"}"

        progressBar!!.progress = (timerLengthSeconds - (secondsRemaining * 1000)).toInt()

    }

    private fun lengthAlarmTimer(): Long{
        return if(Variables.securityButtonViewSelected!!
                .alarmaretardada == 0){
            (Variables.securityButtonViewSelected!!.count * 1000).toLong()
        }else{
            (Variables.securityButtonViewSelected!!.tiempodefectodemoraminutos * 60000)
                .toLong()
        }
    }

    private fun lengthPreTimer(): Long{
        return (Variables.securityButtonViewSelected!!.time * 1000).toLong()
    }

    private fun lengthDeadTimer(): Long{
        return (Variables.securityButtonViewSelected!!.hombremuertovalor * 6000).toLong()
    }

    fun initTimerOfDead(){
        if (verifyManDead()){
            // Send data
            eventObserver.value = Variables.LastSentEvent.DEAD.event
            eventObserver.observableLocation.onNext(eventObserver.value!!)
            startDeadTimer()

        }
    }

    @Suppress("DEPRECATION")
    private fun verifyManDead(): Boolean{
        if (Variables.securityButtonViewSelected!!.hombremuerto == 1){

            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val day = cal.get(Calendar.DAY_OF_WEEK)
            val current = (hour * 60 + minute).toLong()

            val initHour1 = Variables.securityButtonViewSelected!!.calendariohoraini1
            val initHour2 = Variables.securityButtonViewSelected!!.calendariohoraini2
            val endHour1 = Variables.securityButtonViewSelected!!.calendariohorafin1
            val endHour2 = Variables.securityButtonViewSelected!!.calendariohorafin2
            if (!initHour1.isNullOrEmpty() && !initHour2.isNullOrEmpty() &&
                    !endHour1.isNullOrEmpty() && !endHour2.isNullOrEmpty()){

                val initHour1ToLong =  initHour1.getDateWithServerTimeStamp()!!
                    .hours.toLong() * 60 + initHour1.getDateWithServerTimeStamp()!!
                    .minutes.toLong()
                val initHour2ToLong =  initHour2.getDateWithServerTimeStamp()!!
                    .hours.toLong() * 60 + initHour2.getDateWithServerTimeStamp()!!
                    .minutes.toLong()
                val endHour1ToLong =  endHour1.getDateWithServerTimeStamp()!!
                    .hours.toLong() * 60 + endHour1.getDateWithServerTimeStamp()!!
                    .minutes.toLong()
                val endHour2ToLong =  endHour2.getDateWithServerTimeStamp()!!
                    .hours.toLong() * 60 + endHour2.getDateWithServerTimeStamp()!!
                    .minutes.toLong()

                if (((current in (initHour1ToLong + 1)..(endHour1ToLong - 1)) ||
                    (current in (initHour2ToLong + 1)..(endHour2ToLong - 1))) &&
                    dayOfWeek(day)){
                    return true
                }

            }

        }
        return false
    }

    private fun dayOfWeek(day: Int): Boolean{
        when(day){
            1-> if(Variables.securityButtonViewSelected!!.calendariodomingo == 1) return true
            2-> if(Variables.securityButtonViewSelected!!.calendariolunes == 1) return true
            3-> if(Variables.securityButtonViewSelected!!.calendariomartes == 1) return true
            4-> if(Variables.securityButtonViewSelected!!.calendariomiercoles == 1) return true
            5-> if(Variables.securityButtonViewSelected!!.calendariojueves == 1) return true
            6-> if(Variables.securityButtonViewSelected!!.calendarioviernes == 1) return true
            7-> if(Variables.securityButtonViewSelected!!.calendariosabado == 1) return true
        }
        return false
    }

    private fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        )
        //dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        return try {
            dateFormat.parse(this)
        } catch (e: ParseException) {
            null
        }
    }

    fun resetTimers(){
        try {
            timer.cancel()
            deadTimer.cancel()
            timerState = TimerState.Stopped
            timerStatePre = TimerState.Stopped
            initTimerOfDead()
            /*GlobalScope.launch {
                initTimerOfDead()
            }*/

        }catch (ex: UninitializedPropertyAccessException){
            println(ex.message)
        }
    }

    fun destroy(){
        try {
            timer.cancel()
            deadTimer.cancel()
            timerState = TimerState.Stopped
            timerStatePre = TimerState.Stopped

        }catch (ex: UninitializedPropertyAccessException){
            println(ex.message)
        }

    }

}