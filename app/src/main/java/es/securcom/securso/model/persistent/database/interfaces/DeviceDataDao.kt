package es.securcom.securso.model.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import es.securcom.securso.model.persistent.database.entity.DeviceData

@Dao
interface DeviceDataDao {

    @Query("SELECT * FROM deviceData")
    fun getAll(): LiveData<List<DeviceData>>

    @Query("UPDATE deviceData SET updated_at = :update_at WHERE id LIKE :id")
    fun updateDate(update_at: String, id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(deviceData: DeviceData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deviceData: DeviceData)

}