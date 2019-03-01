package es.securcom.securso.presentation.data

import android.os.Parcel
import es.securcom.securso.presentation.plataform.KParcelable
import es.securcom.securso.presentation.plataform.parcelableCreator

class SecurityOptionsView(var pk: String?,
                          var id: Int,
                          var craId: Int,
                          var evcode: String?,
                          var descr: String?,
                          var icon: String?,
                          var color: String?,
                          var time: Int,
                          var count: Int,
                          var audio: Int,
                          var snap: Int,
                          var alarmaretardada: Int,
                          var notificarprealarma: Int,
                          var tiempodefectodemoraminutos: Int,
                          var hombremuerto: Int,
                          var hombremuertovalor: Int,
                          var calendariolunes: Int,
                          var calendariomartes: Int,
                          var calendariomiercoles: Int,
                          var calendariojueves: Int,
                          var calendarioviernes: Int,
                          var calendariosabado: Int,
                          var calendariodomingo: Int,
                          var calendariohoraini1: String?,
                          var calendariohorafin1: String?,
                          var calendariohoraini2: String?,
                          var calendariohorafin2: String?): KParcelable {


    companion object {
        @JvmField val CREATOR = parcelableCreator(
            ::SecurityOptionsView)
    }

    constructor(parcel: Parcel): this(parcel.readString(), parcel.readInt(), parcel.readInt(),
        parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(),
        parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeString(pk)
            writeInt(id)
            writeInt(craId)
            writeString(evcode)
            writeString(descr)
            writeString(icon)
            writeString(color)
            writeInt(time)
            writeInt(count)
            writeInt(audio)
            writeInt(snap)
            writeInt(alarmaretardada)
            writeInt(notificarprealarma)
            writeInt(tiempodefectodemoraminutos)
            writeInt(hombremuerto)
            writeInt(hombremuertovalor)
            writeInt(calendariolunes)
            writeInt(calendariomartes)
            writeInt(calendariomiercoles)
            writeInt(calendariojueves)
            writeInt(calendarioviernes)
            writeInt(calendariosabado)
            writeInt(calendariodomingo)
            writeString(calendariohoraini1)
            writeString(calendariohorafin1)
            writeString(calendariohoraini2)
            writeString(calendariohorafin2)

        }
    }
}