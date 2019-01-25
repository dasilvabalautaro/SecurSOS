package es.securcom.secursos.model.persistent.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "securityOptionsData")
data class SecurityOptionsData(@PrimaryKey var id: String = "",
                               @ColumnInfo(name = "alarmCenterId") var alarmCenterId: Int = 0,
                               @ColumnInfo(name = "description") var description: String = "",
                               @ColumnInfo(name = "time") var time: Int = 0,
                               @ColumnInfo(name = "count") var count: Int = 0,
                               @ColumnInfo(name = "audio") var audio: Int = 0,
                               @ColumnInfo(name = "snap") var snap: Int = 0,
                               @ColumnInfo(name = "delayedAlarm") var delayedAlarm: Int = 0,
                               @ColumnInfo(name = "notificationPre") var notificationPre: Int = 0) {
}