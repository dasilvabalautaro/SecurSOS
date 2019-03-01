package es.securcom.securso.presentation.data

import android.os.Parcel
import es.securcom.securso.presentation.plataform.KParcelable
import es.securcom.securso.presentation.plataform.parcelableCreator
import es.securcom.securso.presentation.plataform.readBoolean
import es.securcom.securso.presentation.plataform.writeBoolean

data class BodyView(val error: Boolean,
                    val message: String?,
                    val device: String?,
                    val cra: String?): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::BodyView)
    }

    constructor(parcel: Parcel):
            this(parcel.readBoolean(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString())
/*
                parcel.readValue(DevicesEntity::class.java.classLoader) as DevicesEntity?,
                parcel.readValue(CraEntity::class.java.classLoader) as CraEntity?
*/

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeBoolean(error)
            writeString(message)
            writeValue(device)
            writeValue(cra)
        }
    }

}