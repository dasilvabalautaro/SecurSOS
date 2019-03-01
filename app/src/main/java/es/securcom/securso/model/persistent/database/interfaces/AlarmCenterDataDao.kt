package es.securcom.securso.model.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import es.securcom.securso.model.persistent.database.entity.AlarmCenterData

@Dao
interface AlarmCenterDataDao {

    @Query("SELECT * FROM alarmCenterData")
    fun getAll(): LiveData<List<AlarmCenterData>>

    @Query("UPDATE alarmCenterData SET updated = :updated WHERE id LIKE :id")
    fun updateDate(updated: String, id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(alarmCenter: AlarmCenterData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarmCenter: AlarmCenterData)
}