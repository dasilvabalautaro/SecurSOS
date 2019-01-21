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
                      @ColumnInfo(name = "identifier") var identifier: String = ""){
}