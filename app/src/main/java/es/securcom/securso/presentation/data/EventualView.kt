package es.securcom.securso.presentation.data

import android.os.Parcel
import es.securcom.securso.presentation.plataform.KParcelable
import es.securcom.securso.presentation.plataform.parcelableCreator

class EventualView(var id: Int,
                   var batteryLevel: Float,
                   var speed: Float,
                   var latitude: Double,
                   var longitude: Double,
                   var heading: Float,
                   var lastUpdate: Double,
                   var lowBatteryAlarmValue: Int): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::EventualView)
    }

    constructor(parcel: Parcel): this(parcel.readInt(), parcel.readFloat(),
        parcel.readFloat(), parcel.readDouble(), parcel.readDouble(),
        parcel.readFloat(), parcel.readDouble(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeFloat(batteryLevel)
            writeFloat(speed)
            writeDouble(latitude)
            writeDouble(longitude)
            writeFloat(heading)
            writeDouble(lastUpdate)
            writeInt(lowBatteryAlarmValue)
        }
    }
}