package es.securcom.securso.model.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagerPendingService @Inject constructor(private val context: Context){

    private var jobScheduler: JobScheduler? = null
    private var jobInfo: JobInfo? = null
    private val jobId = 1338

    init {
        jobInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobInfo.Builder(jobId, ComponentName(context,
                JobPendingService::class.java)
            )
                .setPeriodic((15 * 60 * 1000).toLong(), (7 * 60 * 1000).toLong())
                .build()

        } else {
            JobInfo.Builder(jobId, ComponentName(context,
                JobPendingService::class.java)
            )
                .setPeriodic((60 * 1000).toLong())
                .build()
        }

    }

    fun start(): Boolean{
        jobScheduler = context.getSystemService(Context
            .JOB_SCHEDULER_SERVICE) as JobScheduler
        return when {
            jobScheduler!!.schedule(jobInfo!!) <= 0 -> false
            else -> true
        }

    }

    fun cancel(){
        if (jobScheduler != null){
            jobScheduler!!.cancelAll()
        }

    }

    fun isJobServiceOn(): Boolean {
        val scheduler = context.getSystemService(Context
            .JOB_SCHEDULER_SERVICE) as JobScheduler

        var hasBeenScheduled = false

        for (jobInfo in scheduler.allPendingJobs) {
            if (jobInfo.id == jobId) {
                hasBeenScheduled = true
                break
            }
        }

        return hasBeenScheduled
    }
}