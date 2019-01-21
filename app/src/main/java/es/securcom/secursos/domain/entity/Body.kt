package es.securcom.secursos.domain.entity

import es.securcom.secursos.extension.empty

data class Body(val error: Boolean, val message: String,
                val device: String?, val cra: String?) {
    companion object {
        fun empty() = Body(false, String.empty(),
            String.empty(), String.empty())
    }
}