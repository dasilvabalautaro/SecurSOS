package es.securcom.securso.model.persistent.network

import es.securcom.securso.model.persistent.network.interfaces.ResponseApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRequest @Inject constructor(retrofit: Retrofit): ResponseApi {
    private val responseApi by lazy { retrofit.create(ResponseApi::class.java) }

    override fun result(url: String) = responseApi.result(url)
}