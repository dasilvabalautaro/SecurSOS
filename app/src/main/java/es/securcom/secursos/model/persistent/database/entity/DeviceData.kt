package es.securcom.secursos.model.persistent.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "deviceData")
data class DeviceData(@PrimaryKey var id: Int = 0,
                      @ColumnInfo(name = "phone") var phone: String = "",
                      @ColumnInfo(name = "fullName") var fullName: String = "",
                      @ColumnInfo(name = "serviceNumber") var serviceNumber: String = "",
                      @ColumnInfo(name = "cra_id") var cra_id: Int = 0,
                      @ColumnInfo(name = "created_at") var created_at: String = "",
                      @ColumnInfo(name = "updated_at") var updated_at: String = "",
                      @ColumnInfo(name = "identifier") var identifier: String = "",
                      @ColumnInfo(name = "button1") var button1: Int = 0,
                      @ColumnInfo(name = "button2") var button2: Int = 0,
                      @ColumnInfo(name = "button3") var button3: Int = 0,
                      @ColumnInfo(name = "button4") var button4: Int = 0,
                      @ColumnInfo(name = "active") var active: Int = 0,
                      @ColumnInfo(name = "lang") var lang: String = "",
                      @ColumnInfo(name = "lowBatteryAlert") var lowBatteryAlert: Int = 0,
                      @ColumnInfo(name = "lowBatteryAlertValue") var lowBatteryAlertValue: Int = 0,
                      @ColumnInfo(name = "lowBatteryAlarm") var lowBatteryAlarm: Int = 0,
                      @ColumnInfo(name = "lowBatteryAlarmValue") var lowBatteryAlarmValue: Int = 0,
                      @ColumnInfo(name = "lowSignalAlert") var lowSignalAlert: Int = 0,
                      @ColumnInfo(name = "lowSignalAlertValue") var lowSignalAlertValue: Int = 0,
                      @ColumnInfo(name = "reportInitApp") var reportInitApp: Int = 0,
                      @ColumnInfo(name = "reportCloseApp") var reportCloseApp: Int = 0){
}

