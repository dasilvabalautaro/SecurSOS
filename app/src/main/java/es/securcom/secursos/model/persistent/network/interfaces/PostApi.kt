package es.securcom.secursos.model.persistent.network.interfaces

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

internal interface PostApi {
    @POST fun send(@Url url: String, @Body body: RequestBody): Call<String>
}