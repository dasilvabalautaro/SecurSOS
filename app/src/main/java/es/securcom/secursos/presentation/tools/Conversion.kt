package es.securcom.secursos.presentation.tools

import com.google.gson.Gson
import es.securcom.secursos.model.persistent.caching.Constants
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.presentation.data.AlarmCenterView
import es.securcom.secursos.presentation.data.DeviceView
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONException

object Conversion {

    fun stringToJson(stringJson: String): JSONObject?{
        val gSon = Gson()
        return when {
            stringJson.isNotEmpty() -> JSONObject(gSon.toJson(stringJson))
            else -> null
        }
    }

    fun getValueOfJson(jsonObject: JSONObject): MutableMap<String, Any?>{
        val map = mutableMapOf<String, Any?>()

        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            try {
                val value = jsonObject.get(key)
                map[key] = value
            } catch (e: JSONException) {
                println(e.message)
            }

        }

        return map
    }

    private fun setAlarmCenterView(map: MutableMap<String, Any?>): AlarmCenterView{
        return AlarmCenterView(0, map["name"] as String?, map["phone"] as String?,
            map["email"] as String?, map["logo"] as String?, map["color"] as String?,
            map["web"] as String?, map["ip1"] as String?,
            map["port1"] as String?, map["ip2"] as String?, map["port2"] as String?,
            map["created_at"] as String?, map["updated_at"] as String?, map["info"] as String?,
            map["loctime"] as Int, map["signaltype"] as Int, map["lowBattery"] as Int,
            map["alertabajabateriavalor"] as Int, map["alarmabajabateria"] as Int,
            map["alarmabajabateriavalor"] as Int, map["bajacobertura"] as Int,
            map["bajacoberturavalor"] as Int, map["line"] as String?)
    }

    fun matchAlarmCenterView(cra: String): AlarmCenterView?{
        val objJson = stringToJson(cra)
        if (objJson != null){
            val map = getValueOfJson(objJson)
            return setAlarmCenterView(map)
        }
        return null
    }

    private fun setDeviceView(map: MutableMap<String, Any?>): DeviceView{
        return DeviceView(map["id"] as Int, map["phone"] as String, map["fullname"] as String,
            map["servicenumber"] as String, map["cra_id"] as Int,
            map["created_at"] as String, map["updated_at"] as String, map["identifier"] as String)
    }

    fun matchDeviceView(device: String): DeviceView?{
        val objJson = stringToJson(device)
        if (objJson != null){
            val map = getValueOfJson(objJson)
            return setDeviceView(map)
        }
        return null

    }

    fun createRequestBody(body: String): RequestBody{
        val contentType = "text/xml"

        return RequestBody.create(
            MediaType.parse(contentType),
            (body)
        )

    }

    fun getUrlPort(): String?{
        return when {
            Variables.alarmCenter!!.ipPrimary!!.isNotEmpty() &&
                    Variables.alarmCenter!!.portPrimary!!.isNotEmpty() ->
                Variables.alarmCenter!!.ipPrimary + ":" +
                        Variables.alarmCenter!!.portPrimary
            Variables.alarmCenter!!.ipSecondary!!.isNotEmpty() &&
                    Variables.alarmCenter!!.portSecondary!!.isNotEmpty() ->
                Variables.alarmCenter!!.ipSecondary + ":" +
                        Variables.alarmCenter!!.portSecondary
            else -> null
        }
    }

    fun getUrlIdentifier(): String{
        val servicesNumber = Variables.devicesView!!.serviceNumber
        val identifier = Variables.devicesView!!.identifier
        return String.format("${Constants.urlBase}${Constants.deviceServiceNumber}" +
                "$servicesNumber/$identifier")

    }
}