package es.securcom.secursos.domain.entity

import es.securcom.secursos.extension.empty
import es.securcom.secursos.model.persistent.network.entity.CraEntity
import es.securcom.secursos.model.persistent.network.entity.DevicesEntity

data class Body(val error: Boolean, val message: String,
                val device: Any?, val cra: Any?) {
    companion object {
        fun empty() = Body(false, String.empty(),
            null, null)
    }
}