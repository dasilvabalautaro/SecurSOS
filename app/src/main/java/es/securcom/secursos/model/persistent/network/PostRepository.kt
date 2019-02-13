package es.securcom.secursos.model.persistent.network

import es.securcom.secursos.domain.functional.Either
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.caching.Variables
import es.securcom.secursos.model.persistent.network.entity.PendingShipping
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

interface PostRepository {
    fun results(url: String, body: RequestBody): Either<Failure, String>

    class PostSend @Inject constructor(private val networkHandler: NetworkHandler,
                                       private val postRequest: PostRequest):
        PostRepository{
        override fun results(url: String, body: RequestBody): Either<Failure, String> {
            return when (networkHandler.isConnected){
                true -> request(postRequest.send(url, body), {toString()}, "" )
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T):
                Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Right(transform((response.body() ?: default)))
                    false -> Either.Left(Failure.ServerError())
                }
            } catch (exception: Throwable) {
                println("ERROR POST: ${exception.message}")
                Either.Left(Failure.ServerError())
            }
        }


    }
}