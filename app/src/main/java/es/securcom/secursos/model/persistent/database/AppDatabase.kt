package es.securcom.secursos.model.persistent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import es.securcom.secursos.model.persistent.database.entity.AlarmCenterData
import es.securcom.secursos.model.persistent.database.entity.DeviceData
import es.securcom.secursos.model.persistent.database.entity.EventualData
import es.securcom.secursos.model.persistent.database.interfaces.AlarmCenterDataDao
import es.securcom.secursos.model.persistent.database.interfaces.DeviceDataDao
import es.securcom.secursos.model.persistent.database.interfaces.EventualDataDao

@Database(entities = [AlarmCenterData::class, EventualData::class, DeviceData::class],
    version = 1, exportSchema = false)

abstract class AppDatabase: RoomDatabase() {
    abstract fun alarmCenterDataDao(): AlarmCenterDataDao
    abstract fun eventualDataDao(): EventualDataDao
    abstract fun deviceDataDao(): DeviceDataDao
}