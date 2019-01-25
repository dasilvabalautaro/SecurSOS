package es.securcom.secursos.model.persistent.database.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import es.securcom.secursos.model.persistent.database.entity.SecurityOptionsData

@Dao
interface SecurityOptionsDataDao {
    @Query("SELECT * FROM securityOptionsData")
    fun getAll(): LiveData<List<SecurityOptionsData>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(securityOptionsData: SecurityOptionsData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(securityOptionsData: SecurityOptionsData)
}