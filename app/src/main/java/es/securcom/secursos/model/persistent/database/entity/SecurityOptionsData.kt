package es.securcom.secursos.model.persistent.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "securityOptionsData")
data class SecurityOptionsData(@PrimaryKey var pk: String = "",
                               @ColumnInfo(name = "id") var id: Int = 0,
                               @ColumnInfo(name = "craId") var craId: Int = 0,
                               @ColumnInfo(name = "evcode") var evcode: String = "",
                               @ColumnInfo(name = "descr") var descr: String = "",
                               @ColumnInfo(name = "icon") var icon: String = "",
                               @ColumnInfo(name = "color") var color: String = "",
                               @ColumnInfo(name = "time") var time: Int = 0,
                               @ColumnInfo(name = "count") var count: Int = 0,
                               @ColumnInfo(name = "audio") var audio: Int = 0,
                               @ColumnInfo(name = "snap") var snap: Int = 0,
                               @ColumnInfo(name = "delayedAlarm") var delayedAlarm: Int = 0,
                               @ColumnInfo(name = "notificationPre") var notificationPre: Int = 0,
                               @ColumnInfo(name = "tiempodefectodemoraminutos") var tiempodefectodemoraminutos: Int = 0,
                               @ColumnInfo(name = "hombremuerto") var hombremuerto: Int = 0,
                               @ColumnInfo(name = "hombremuertovalor") var hombremuertovalor: Int = 0,
                               @ColumnInfo(name = "calendariolunes") var calendariolunes: Int = 0,
                               @ColumnInfo(name = "calendariomartes") var calendariomartes: Int = 0,
                               @ColumnInfo(name = "calendariomiercoles") var calendariomiercoles: Int = 0,
                               @ColumnInfo(name = "calendariojueves") var calendariojueves: Int = 0,
                               @ColumnInfo(name = "calendarioviernes") var calendarioviernes: Int = 0,
                               @ColumnInfo(name = "calendariosabado") var calendariosabado: Int = 0,
                               @ColumnInfo(name = "calendariodomingo") var calendariodomingo: Int = 0,
                               @ColumnInfo(name = "calendariohoraini1") var calendariohoraini1: String = "",
                               @ColumnInfo(name = "calendariohorafin1") var calendariohorafin1: String = "",
                               @ColumnInfo(name = "calendariohoraini2") var calendariohoraini2: String = "",
                               @ColumnInfo(name = "calendariohorafin2") var calendariohorafin2: String = "") {
}

