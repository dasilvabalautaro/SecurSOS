package es.securcom.secursos.presentation.data

import android.os.Parcel
import es.securcom.secursos.presentation.plataform.KParcelable
import es.securcom.secursos.presentation.plataform.parcelableCreator

class SecurityButtonView(var description: String?): KParcelable {

    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::SecurityButtonView)
    }

    constructor(parcel: Parcel): this(parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(description)
        }
    }
}