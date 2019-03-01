package es.securcom.securso.model.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import es.securcom.securso.model.persistent.database.entity.EventualData

@Dao
interface EventualDataDao {

    @Query("SELECT * FROM eventualData")
    fun getAll(): LiveData<List<EventualData>>

    @Query("UPDATE eventualData SET lastUpdate = :lastUpdate WHERE id LIKE :id")
    fun updateDate(lastUpdate: Double, id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(eventualData: EventualData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(eventualData: EventualData)
}