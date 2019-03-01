package es.securcom.securso.presentation.tools

import es.securcom.securso.model.persistent.caching.Constants
import es.securcom.securso.model.persistent.caching.Variables
import es.securcom.securso.presentation.data.AlarmCenterView
import es.securcom.securso.presentation.data.DeviceView
import es.securcom.securso.presentation.data.SecurityOptionsView
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONException

object Conversion {

    private lateinit var mapAlarmCenterView: MutableMap<String, Any?>
    lateinit var mapDeviceView: MutableMap<String, Any?>

    private fun stringToJson(stringJson: String): JSONObject?{
        //val gSon = Gson()
        return when {
            stringJson.isNotEmpty() -> JSONObject(stringJson)
            else -> null
        }
        //JSONObject(gSon.toJson(stringJson))
    }

    fun getLabelButton(keyLanguage: String, stringJson: String): String{
        val jsonObject = stringToJson(stringJson)
        if (jsonObject != null){
            val iterator = jsonObject.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                try {
                    if (key == keyLanguage){
                        return jsonObject.get(key).toString()
                    }
                } catch (e: JSONException) {
                    println(e.message)
                }

            }

        }
        return ""
    }

    private fun getValueOfJson(jsonObject: JSONObject): MutableMap<String, Any?>{
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

    private fun transformJsonToString(jsonObject: JSONObject): String{
        var result = ""

        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            try {
                val value = jsonObject.get(key)
                result += "$key: $value\n"

            } catch (e: JSONException) {
                println(e.message)
            }

        }

        return result
    }

    fun getValueStringOfJson(value: String): String?{
        val objJson = stringToJson(value)
        if (objJson != null){
            return transformJsonToString(objJson)
        }
        return null
    }


    private fun setAlarmCenterView(map: MutableMap<String, Any?>): AlarmCenterView{
        return AlarmCenterView(
            if (map.containsKey("id")) (map["id"] as Double).toInt() else 0,
            if (map.containsKey("name")) map["name"] as String? else "",
            if (map.containsKey("phone")) map["phone"] as String? else "",
            if (map.containsKey("email")) map["email"] as String? else "",
            if (map.containsKey("logo")) map["logo"] as String? else "",
            if (map.containsKey("color")) map["color"] as String? else "",
            if (map.containsKey("web")) map["web"] as String? else "",
            if (map.containsKey("ip1")) map["ip1"] as String? else "",
            if (map.containsKey("port1")) map["port1"] as String? else "",
            if (map.containsKey("ip2")) map["ip2"] as String? else "",
            if (map.containsKey("port2")) map["port2"] as String? else "",
            if (map.containsKey("created_at")) map["created_at"] as String? else "",
            if (map.containsKey("updated_at")) map["updated_at"] as String? else "",
            if (map.containsKey("info")) map["info"] as String? else "",
            if (map.containsKey("loctime")) (map["loctime"] as Double).toInt() else 0,
            if (map.containsKey("signaltype")) (map["signaltype"] as Double).toInt() else 0,
            if (map.containsKey("lowBattery")) (map["lowBattery"] as Double).toInt() else 0,
            if (map.containsKey("alertabajabateriavalor")) (map["alertabajabateriavalor"] as Double).toInt() else 0,
            if (map.containsKey("alarmabajabateria")) (map["alarmabajabateria"] as Double).toInt() else 0,
            if (map.containsKey("alarmabajabateriavalor")) (map["alarmabajabateriavalor"] as Double).toInt() else 0,
            if (map.containsKey("bajacobertura")) (map["bajacobertura"] as Double).toInt() else 0,
            if (map.containsKey("bajacoberturavalor")) (map["bajacoberturavalor"] as Double).toInt() else 0,
            if (map.containsKey("line")) map["line"] as String? else "",
            if (map.containsKey("active")) (map["active"] as Double).toInt() else 0,
            if (map.containsKey("devmax")) map["devmax"] as String? else "",
            if (map.containsKey("updatetime")) (map["updatetime"] as Double).toInt() else 0,
            if (map.containsKey("reportarinicioaplicacion")) (map["reportarinicioaplicacion"] as Double).toInt() else 0,
            if (map.containsKey("reportarcierreaplicacion")) (map["reportarcierreaplicacion"] as Double).toInt() else 0)

    }

    fun matchedAlarmCenterView(cra: String): AlarmCenterView?{
        val objJson = stringToJson(cra)
        if (objJson != null){
            val map = getValueOfJson(objJson)
            this.mapAlarmCenterView = map
            return setAlarmCenterView(map)
        }
        return null
    }

    private fun setDeviceView(map: MutableMap<String, Any?>): DeviceView{

        return DeviceView(
            if (map.containsKey("id")) (map["id"] as Double).toInt() else 0,
            if (map.containsKey("phone")) map["phone"] as String else "",
            if (map.containsKey("fullname")) map["fullname"] as String else "",
            if (map.containsKey("servicenumber")) map["servicenumber"] as String else "",
            if (map.containsKey("cra_id")) (map["cra_id"] as Double).toInt() else 0,
            if (map.containsKey("created_at")) map["created_at"] as String else "",
            if (map.containsKey("updated_at")) map["updated_at"] as String else "",
            if (map.containsKey("identifier")) map["identifier"] as String else "",
            if (map.containsKey("button1")) (map["button1"] as Double).toInt() else 0,
            if (map.containsKey("button2")) (map["button2"] as Double).toInt() else 0,
            if (map.containsKey("button3")) (map["button3"] as Double).toInt() else 0,
            if (map.containsKey("button4")) (map["button4"] as Double).toInt() else 0,
            if (map.containsKey("active")) (map["active"] as Double).toInt() else 0,
            if (map.containsKey("lang")) map["lang"] as String else "",
            if (map.containsKey("alertabajabateria")) (map["alertabajabateria"] as Double).toInt() else 0,
            if (map.containsKey("alertabajabateriavalor")) (map["alertabajabateriavalor"] as Double).toInt() else 0,
            if (map.containsKey("alarmabajabateria")) (map["alarmabajabateria"] as Double).toInt() else 0,
            if (map.containsKey("alarmabajabateriavalor")) (map["alarmabajabateriavalor"] as Double).toInt() else 0,
            if (map.containsKey("bajacobertura")) (map["bajacobertura"] as Double).toInt() else 0,
            if (map.containsKey("bajacoberturavalor")) (map["bajacoberturavalor"] as Double).toInt() else 0,
            if (map.containsKey("reportarinicioaplicacion")) (map["reportarinicioaplicacion"] as Double).toInt() else 0,
            if (map.containsKey("reportarcierreaplicacion")) (map["reportarcierreaplicacion"] as Double).toInt() else 0)

    }


    fun matchedSecurityOptionsView(prefix: String): SecurityOptionsView?{
        return setSecurityOptionsView(this.mapDeviceView, this.mapAlarmCenterView,
             prefix)
    }


    private fun setSecurityOptionsView(mapDevice: MutableMap<String, Any?>,
                                       mapCra: MutableMap<String, Any?>, prefix: String):
            SecurityOptionsView{
        val pk = (mapDevice["id"] as Double).toInt().toString() + prefix

        return SecurityOptionsView(pk,
            if (mapDevice.containsKey("id")) (mapDevice["id"] as Double).toInt() else 0,
            if (mapDevice.containsKey("cra_id")) (mapDevice["cra_id"] as Double).toInt() else 0,
            if (mapCra.containsKey("${prefix}evcode")) (mapCra["${prefix}evcode"] as String?) else "",
            if (mapCra.containsKey("${prefix}descr")) (mapCra["${prefix}descr"] as String?) else "",
            if (mapCra.containsKey("${prefix}icon")) (mapCra["${prefix}icon"] as String?) else "",
            if (mapCra.containsKey("${prefix}color")) (mapCra["${prefix}color"] as String?) else "",
            if (mapCra.containsKey("${prefix}time")) (mapCra["${prefix}time"] as Double).toInt() else 0,
            if (mapCra.containsKey("${prefix}count")) (mapCra["${prefix}count"] as Double).toInt() else 0,
            if (mapCra.containsKey("${prefix}audio")) (mapCra["${prefix}audio"] as Double).toInt() else 0,
            if (mapCra.containsKey("${prefix}snap")) (mapCra["${prefix}snap"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}alarmaretardada")) (mapDevice["${prefix}alarmaretardada"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}notificarprealarma")) (mapDevice["${prefix}notificarprealarma"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}tiempodefectodemoraminutos")) (mapDevice["${prefix}tiempodefectodemoraminutos"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}hombremuerto")) (mapDevice["${prefix}hombremuerto"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}hombremuertovalor")) (mapDevice["${prefix}hombremuertovalor"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariolunes")) (mapDevice["${prefix}calendariolunes"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariomartes")) (mapDevice["${prefix}calendariomartes"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariomiercoles")) (mapDevice["${prefix}calendariomiercoles"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariojueves")) (mapDevice["${prefix}calendariojueves"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendarioviernes")) (mapDevice["${prefix}calendarioviernes"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariosabado")) (mapDevice["${prefix}calendariosabado"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariodomingo")) (mapDevice["${prefix}calendariodomingo"] as Double).toInt() else 0,
            if (mapDevice.containsKey("${prefix}calendariohoraini1")) (mapDevice["${prefix}calendariohoraini1"] as String?) else "",
            if (mapDevice.containsKey("${prefix}calendariohorafin1")) (mapDevice["${prefix}calendariohorafin1"] as String?) else "",
            if (mapDevice.containsKey("${prefix}calendariohoraini2")) (mapDevice["${prefix}calendariohoraini2"] as String?) else "",
            if (mapDevice.containsKey("${prefix}calendariohorafin2")) (mapDevice["${prefix}calendariohorafin2"] as String?) else "")


    }


    fun matchedDeviceView(device: String): DeviceView?{
        val objJson = stringToJson(device)
        if (objJson != null){
            val map = getValueOfJson(objJson)
            this.mapDeviceView = map
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
        val servicesNumber = if(Variables.devicesView != null)
            Variables.devicesView!!.serviceNumber else ""
        val identifier = if(Variables.devicesView != null)
            Variables.devicesView!!.identifier else ""
        return String.format("${Constants.urlBase}${Constants.deviceServiceNumber}" +
                "$servicesNumber/$identifier")

    }
}