package es.securcom.securso.model.persistent.network.entity

import es.securcom.securso.domain.entity.Body

data class BodyEntity(private val error: Boolean, private val message: String,
                      private val device: Any?, private val cra: Any?) {

    fun toBody() = Body(error, message, device, cra)

}