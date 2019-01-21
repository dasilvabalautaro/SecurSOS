package es.securcom.secursos.model.persistent.network


import es.securcom.secursos.model.persistent.network.interfaces.PostApi
import okhttp3.RequestBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRequest @Inject constructor(retrofit: Retrofit): PostApi {

    private val postApi by lazy { retrofit.create(PostApi::class.java) }

    override fun send(url: String, body: RequestBody) = postApi.send(url, body)
}