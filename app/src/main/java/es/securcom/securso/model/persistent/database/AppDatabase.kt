package es.securcom.securso.model.persistent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import es.securcom.securso.model.persistent.database.entity.AlarmCenterData
import es.securcom.securso.model.persistent.database.entity.DeviceData
import es.securcom.securso.model.persistent.database.entity.EventualData
import es.securcom.securso.model.persistent.database.entity.SecurityOptionsData
import es.securcom.securso.model.persistent.database.interfaces.AlarmCenterDataDao
import es.securcom.securso.model.persistent.database.interfaces.DeviceDataDao
import es.securcom.securso.model.persistent.database.interfaces.EventualDataDao
import es.securcom.securso.model.persistent.database.interfaces.SecurityOptionsDataDao

@Database(entities = [AlarmCenterData::class, EventualData::class,
    DeviceData::class, SecurityOptionsData::class],
    version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun alarmCenterDataDao(): AlarmCenterDataDao
    abstract fun eventualDataDao(): EventualDataDao
    abstract fun deviceDataDao(): DeviceDataDao
    abstract fun securityOptionsDataDao(): SecurityOptionsDataDao
}