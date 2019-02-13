package es.securcom.secursos.model.persistent.network.entity

import es.securcom.secursos.domain.entity.Body

data class BodyEntity(private val error: Boolean, private val message: String,
                      private val device: Any?, private val cra: Any?) {

    fun toBody() = Body(error, message, device, cra)

}