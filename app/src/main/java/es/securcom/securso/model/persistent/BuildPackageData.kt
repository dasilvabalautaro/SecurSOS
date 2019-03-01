package es.securcom.securso.model.persistent

import android.content.Context
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.presentation.tools.Conversion
import java.util.*
import javax.inject.Inject

class BuildPackageData @Inject constructor(private val context: Context) {

    private val startCharacter = String.format("%c", 0x02)
    private val endCharacter = String.format("%c", 0x03)

    var code = ""
    var line = ""
    var comments = ""
    var event = ""


    private fun buildPackageGenericLineWithSignal(pointId: String): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"STOP\"><PointID>%s</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            pointId,
            getUrl(),
            endCharacter
        )
    }

    private fun buildPackagePreAlarmWithOutSignal(preAlarmTimeout: Int): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"PRE\"><PointID>PREALARM + %d</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            preAlarmTimeout,
            endCharacter
        )
    }

    private fun buildPackagePreAlarmWithSignal(preAlarmTimeout: Int): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"PRE\"><PointID>PREALARM + %d</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            preAlarmTimeout,
            getUrl(),
            endCharacter
        )
    }

    fun buildPackagePreAlarm(preAlarmTimeout: Int): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackagePreAlarmWithSignal(preAlarmTimeout)
        }else{
            buildPackagePreAlarmWithOutSignal(preAlarmTimeout)
        }
    }

    private fun buildPackageGenericLineWithOutSignal(pointId: String): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"START\"><PointID>%s</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            pointId,
            endCharacter
        )
    }

    fun buildPackageGenericDataLine(pointId: String): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageGenericLineWithSignal(pointId)
        }else{
            buildPackageGenericLineWithOutSignal(pointId)
        }
    }


    fun buildPackageLocationData(): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildLocationDataWithSignal()
        }else{
            buildLocationDataWithOutSignal()
        }
    }

    fun buildPackageApplicationShutDown(): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageApplicationStopWithSignal()
        }else{
            buildPackageApplicationStop()
        }
    }

    fun buildPackageApplicationInit(): String{
        return buildPackageApplicationStarted()
    }

    fun buildPackageLowBattery(): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageLowBatteryWithSignal()
        }else{
            buildPackageLowBatteryWithOutSignal()
        }

    }

    fun buildPackageBatteryOK(): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageBatteryOkWithSignal()
        }else{
            buildPackageBatteryOkWithOutSignal()
        }
    }

    fun buildPackageSendMessage(message: String): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageSendMessageWithSignal(message)
        }else{
            buildPackageSendMessageWithOutSignal(message)
        }
    }
    private fun buildPackageSendMessageWithSignal(message: String): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"MSG\"><PointID>%s</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            message,
            getUrl(),
            endCharacter
        )
    }

    private fun buildPackageSendMessageWithOutSignal(message: String): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"MSG\"><PointID>%s</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            message,
            endCharacter
        )

    }

    private fun buildPackageLowBatteryWithSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"*L\"><PointID>AVISO BAJA BAT %d%s</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            Variables.eventualData!!.batteryLevel,
            "%",
            getUrl(),
            endCharacter
        )
    }

    private fun buildPackageLowBatteryWithOutSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"*L\"><PointID>AVISO BAJA BAT %d%s</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            Variables.eventualData!!.batteryLevel,
            "%",
            endCharacter
        )

    }

    fun buildPackageLowBatteryAlarm(): String{
        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildPackageLowBatteryAlarmWithSignal()
        }else{
            buildPackageLowBatteryAlarmWithOutSignal()
        }

    }

    private fun buildPackageLowBatteryAlarmWithSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"YM\"><PointID>ALARMA BAJA BAT %d%s</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            Variables.alarmCenter!!.lowBatteryAlarmValue,
            "%",
            getUrl(),
            endCharacter
        )
    }

    private fun buildPackageLowBatteryAlarmWithOutSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"YM\"><PointID>ALARMA BAJA BAT %d%s</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            Variables.alarmCenter!!.lowBatteryAlarmValue,
            "%",
            endCharacter
        )

    }


    private fun buildPackageApplicationStopWithSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"STOP\"><PointID>APP STOPPED</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            getUrl(),
            endCharacter
        )
    }
    private fun buildPackageBatteryOkWithOutSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"*G\"><PointID>BAT OK</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            endCharacter
        )
    }

    private fun buildPackageBatteryOkWithSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"*G\"><PointID>BAT OK</PointID><URL>%s</URL><URLInfo>Localizacion</URLInfo></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            getUrl(),
            endCharacter
        )
    }

    private fun getUrl(): String{
        var url = ""

        when(Variables.alarmCenter!!.latLngType ){
            2 -> {
                url = String.format(
                    Locale.US,
                    "http://maps.google.com/maps?z=11&t=k&q=%f+%f",
                    Variables.eventualData!!.latitude,
                    Variables.eventualData!!.longitude
                )
            }
            3 -> {
                url = String.format(
                    Locale.US,
                    "http://www.bing.com/maps/?v=2&cp=%f~%f&lvl=16&sp=point.%f_%f_Alarm Position",
                    Variables.eventualData!!.latitude,
                    Variables.eventualData!!.longitude,
                    Variables.eventualData!!.latitude,
                    Variables.eventualData!!.longitude
                )
            }
            4 -> {
                url = String.format(
                    Locale.US,
                    "http://www.openstreetmap.org/?mlat=%f&mlon=%f&zoom=16",
                    Variables.eventualData!!.latitude,
                    Variables.eventualData!!.longitude
                )
            }
        }

        return url
    }

    private fun buildLocationDataWithOutSignal(): String{

        this.comments = String.format("<Comment>%s</Comment>", this.comments)

        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><GPS EvType=\"SEC01\" Event=\"%s\"><Latitude>%f</Latitude><Longitude>%f</Longitude><Speed>%f</Speed><Heading>%f deg</Heading><Power>%f%s</Power>%s</GPS></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            this.line,
            this.code,
            Variables.eventualData!!.latitude,
            Variables.eventualData!!.longitude,
            Variables.eventualData!!.speed,
            Variables.eventualData!!.heading,
            Variables.eventualData!!.batteryLevel,
            "%",
            this.comments,
            endCharacter
        )
    }

    private fun buildLocationDataWithSignal(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"%s\"><URL>%s</URL><URLInfo>GPS_Loc</URLInfo><PointID>%s</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            this.line,
            this.code,
            getUrl(),
            this.event,
            endCharacter
        )
    }

    fun buildPackageByEvent(): String{

        this.event = Conversion
            .getValueStringOfJson(Variables.securityButtonViewSelected!!.descr?:"")?:""

        return if (Variables.alarmCenter!!.latLngType != 0 &&
            Variables.alarmCenter!!.latLngType != 1){
            buildLocationDataWithSignal()
        }else{
            buildLocationDataWithOutSignal()
        }

    }

    fun buildPackageMedia(fileSize: Long, base64Length: Int,
                          string64Media: String): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"IMAGE\"><Zone>1</Zone></Signal><Binary Type=\"1008\" Ext=\"JPG\" DataType=\"V\" Length=\"%d\"><Data Length=\"%d\">%s</Data></Binary></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            fileSize,
            base64Length,
            string64Media,
            endCharacter
        )
    }

    private fun buildPackageApplicationStarted(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"START\"><PointID>APP STARTED</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            endCharacter
        )
    }

    private fun buildPackageApplicationStop(): String{
        return String.format(
            Locale.US,
            "%s<?xml version=\"1.0\"?><Packet ID=\"%s\" Line=\"%s\"><Signal EvType=\"SEC01\" Event=\"START\"><PointID>APP STOPPED</PointID></Signal></Packet>%s",
            startCharacter,
            Variables.devicesView!!.serviceNumber,
            line,
            endCharacter
        )
    }
}