package es.securcom.secursos.model.persistent.network

import android.content.Context
import es.securcom.secursos.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler @Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnected
}