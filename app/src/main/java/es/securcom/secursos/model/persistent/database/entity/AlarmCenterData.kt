package es.securcom.secursos.model.persistent.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "alarmCenterData")
data class AlarmCenterData(@PrimaryKey var id: Int = 0,
                           @ColumnInfo(name = "name") var name: String = "",
                           @ColumnInfo(name = "phone") var phone: String = "",
                           @ColumnInfo(name = "email") var email: String = "",
                           @ColumnInfo(name = "logo") var logo: String = "",
                           @ColumnInfo(name = "color") var color: String = "",
                           @ColumnInfo(name = "web") var web: String = "",
                           @ColumnInfo(name = "ipPrimary") var ipPrimary: String = "",
                           @ColumnInfo(name = "portPrimary") var portPrimary: String = "",
                           @ColumnInfo(name = "ipSecondary") var ipSecondary: String = "",
                           @ColumnInfo(name = "portSecondary") var portSecondary: String = "",
                           @ColumnInfo(name = "created") var created: String = "",
                           @ColumnInfo(name = "updated") var updated: String = "",
                           @ColumnInfo(name = "information") var information: String = "",
                           @ColumnInfo(name = "lapseLocation") var lapseLocation: Int = 0,
                           @ColumnInfo(name = "latLngType") var latLngType: Int = 0,
                           @ColumnInfo(name = "lowBatteryAlert") var lowBatteryAlert: Int = 0,
                           @ColumnInfo(name = "lowBatteryAlertValue") var lowBatteryAlertValue: Int = 0,
                           @ColumnInfo(name = "lowBatteryAlarm") var lowBatteryAlarm: Int = 0,
                           @ColumnInfo(name = "lowBatteryAlarmValue") var lowBatteryAlarmValue: Int = 0,
                           @ColumnInfo(name = "lowSignalAlert") var lowSignalAlert: Int = 0,
                           @ColumnInfo(name = "lowSignalAlertValue") var lowSignalAlertValue: Int = 0,
                           @ColumnInfo(name = "line") var line: String = "",
                           @ColumnInfo(name = "active") var active: Int = 0,
                           @ColumnInfo(name = "devMax") var devMax: String = "",
                           @ColumnInfo(name = "updateTime") var updateTime: Int = 0,
                           @ColumnInfo(name = "reportInitApp") var reportInitApp: Int = 0,
                           @ColumnInfo(name = "reportCloseApp") var reportCloseApp: Int = 0) {
}