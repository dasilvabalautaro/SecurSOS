package es.securcom.secursos.model.persistent.network.entity


data class PendingShipping(var url: String,
                           var body: String,
                           var status: Int) {
}