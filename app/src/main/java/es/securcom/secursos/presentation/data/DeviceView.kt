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
                 var identifier: String?): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::DeviceView)
    }

    constructor(parcel: Parcel): this(parcel.readInt(), parcel.readString(),
        parcel.readString(), parcel.readString(), parcel.readInt(),
        parcel.readString(), parcel.readString(), parcel.readString())

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
        }
    }
}