package es.securcom.secursos.presentation.data

import android.os.Parcel
import es.securcom.secursos.presentation.plataform.KParcelable
import es.securcom.secursos.presentation.plataform.parcelableCreator

class SecurityOptionsView(var id: String?,
                          var alarmCenterId: Int,
                          var description: String?,
                          var time: Int,
                          var count: Int,
                          var audio: Int,
                          var snap: Int,
                          var delayedAlarm: Int,
                          var notificationPre: Int): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::SecurityOptionsView)
    }

    constructor(parcel: Parcel): this(parcel.readString(), parcel.readInt(),
        parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(id)
            writeInt(alarmCenterId)
            writeString(description)
            writeInt(time)
            writeInt(count)
            writeInt(audio)
            writeInt(snap)
            writeInt(delayedAlarm)
            writeInt(notificationPre)
        }
    }
}