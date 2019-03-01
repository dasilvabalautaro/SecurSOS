package es.securcom.securso.domain.entity

import es.securcom.securso.extension.empty

data class Body(val error: Boolean, val message: String,
                val device: Any?, val cra: Any?) {
    companion object {
        fun empty() = Body(false, String.empty(),
            null, null)
    }
}