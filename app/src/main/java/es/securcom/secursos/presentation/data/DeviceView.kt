package es.securcom.secursos.presentation.data

import android.os.Parcel
import es.securcom.secursos.presentation.plataform.KParcelable
import es.securcom.secursos.presentation.plataform.parcelableCreator


class DeviceView(var id: Int,
                 var phone: String?,
                 var fullName: String?,
                 var serviceNumber: String?,
                 var cra_id: Int,
                 var created_at: String?,
                 var updated_at: String?,
                 var identifier: String?,
                 var button1: Int,
                 var button2: Int,
                 var button3: Int,
                 var button4: Int,
                 var active: Int,
                 var lang: String?,
                 var lowBatteryAlert: Int,
                 var lowBatteryAlertValue: Int,
                 var lowBatteryAlarm: Int,
                 var lowBatteryAlarmValue: Int,
                 var lowSignalAlert: Int,
                 var lowSignalAlertValue: Int,
                 var reportInitApp: Int,
                 var reportCloseApp: Int): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::DeviceView)
    }

    constructor(parcel: Parcel): this(parcel.readInt(), parcel.readString(),
        parcel.readString(), parcel.readString(), parcel.readInt(),
        parcel.readString(), parcel.readString(), parcel.readString(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(phone)
            writeString(fullName)
            writeString(serviceNumber)
            writeInt(cra_id)
            writeString(created_at)
            writeString(updated_at)
            writeString(identifier)
            writeInt(button1)
            writeInt(button2)
            writeInt(button3)
            writeInt(button4)
            writeInt(active)
            writeString(lang)
            writeInt(lowBatteryAlert)
            writeInt(lowBatteryAlertValue)
            writeInt(lowBatteryAlarm)
            writeInt(lowBatteryAlarmValue)
            writeInt(lowSignalAlert)
            writeInt(lowSignalAlertValue)
            writeInt(reportInitApp)
            writeInt(reportCloseApp)
        }
    }
}