package es.securcom.secursos.model.persistent.network

import es.securcom.secursos.domain.entity.Body
import es.securcom.secursos.domain.functional.Either
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.network.entity.BodyEntity
import retrofit2.Call
import javax.inject.Inject

interface ResultRepository {
    fun results(url: String): Either<Failure, Body>

    class Network @Inject constructor(private val networkHandler: NetworkHandler,
                                      private val getRequest: GetRequest):
        ResultRepository{
        override fun results(url: String): Either<Failure, Body> {
            return when (networkHandler.isConnected){
                true -> request(getRequest.result(url),
                    {it.toBody()}, BodyEntity(true, "", "", "")
                )
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
                Either.Left(Failure.ServerError())
            }
        }

    }
}