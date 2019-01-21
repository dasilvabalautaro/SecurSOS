package es.securcom.secursos.model.persistent.network.interfaces

import es.securcom.secursos.model.persistent.network.entity.BodyEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

internal interface ResponseApi {

    @GET fun result(@Url url: String): Call<BodyEntity>
}