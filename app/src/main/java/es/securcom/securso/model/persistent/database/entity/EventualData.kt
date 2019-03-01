package es.securcom.securso.model.persistent.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "eventualData")
data class EventualData(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                        @ColumnInfo(name = "batteryLevel") var batteryLevel: Float = 0f,
                        @ColumnInfo(name = "speed") var speed: Float = 0f,
                        @ColumnInfo(name = "latitude") var latitude: Double = 0.0,
                        @ColumnInfo(name = "longitude") var longitude: Double = 0.0,
                        @ColumnInfo(name = "heading") var heading: Float = 0f,
                        @ColumnInfo(name = "lastUpdate") var lastUpdate: Double = 0.0,
                        @ColumnInfo(name = "lowBatteryAlarmValue") var lowBatteryAlarmValue: Int = 0){

}