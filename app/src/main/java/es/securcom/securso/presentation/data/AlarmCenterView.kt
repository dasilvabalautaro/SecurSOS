package es.securcom.securso.presentation.data

import android.os.Parcel
import es.securcom.securso.presentation.plataform.KParcelable
import es.securcom.securso.presentation.plataform.parcelableCreator

data class AlarmCenterView(var id: Int,
                           var name: String?,
                           var phone: String?,
                           var email: String?,
                           var logo: String?,
                           var color: String?,
                           var web: String?,
                           var ipPrimary: String?,
                           var portPrimary: String?,
                           var ipSecondary: String?,
                           var portSecondary: String?,
                           var created: String?,
                           var updated: String?,
                           var information: String?,
                           var lapseLocation: Int,
                           var latLngType: Int,
                           var lowBatteryAlert: Int,
                           var lowBatteryAlertValue: Int,
                           var lowBatteryAlarm: Int,
                           var lowBatteryAlarmValue: Int,
                           var lowSignalAlert: Int,
                           var lowSignalAlertValue: Int,
                           var line: String?,
                           var active: Int,
                           var devMax: String?,
                           var updateTime: Int,
                           var reportInitApp: Int,
                           var reportCloseApp: Int): KParcelable {


    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::AlarmCenterView)
    }

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString(),
        parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(),
        parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(),
        parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readString(), parcel.readInt(), parcel.readString(),
        parcel.readInt(), parcel.readInt(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(name)
            writeString(phone)
            writeString(email)
            writeString(logo)
            writeString(color)
            writeString(web)
            writeString(ipPrimary)
            writeString(portPrimary)
            writeString(ipSecondary)
            writeString(portSecondary)
            writeString(created)
            writeString(updated)
            writeString(information)
            writeInt(lapseLocation)
            writeInt(latLngType)
            writeInt(lowBatteryAlert)
            writeInt(lowBatteryAlertValue)
            writeInt(lowBatteryAlarm)
            writeInt(lowBatteryAlarmValue)
            writeInt(lowSignalAlert)
            writeInt(lowSignalAlertValue)
            writeString(line)
            writeInt(active)
            writeString(devMax)
            writeInt(updateTime)
            writeInt(reportInitApp)
            writeInt(reportCloseApp)
        }
    }

}