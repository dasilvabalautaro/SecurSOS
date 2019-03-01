package es.securcom.securso.domain.interactor

import es.securcom.securso.domain.entity.Body
import es.securcom.securso.domain.functional.Either
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.network.ResultRepository
import javax.inject.Inject

class GetRequestUseCase @Inject constructor(private val resultRepository:
                                                ResultRepository):
    UseCase<Body, String>() {

    override suspend fun run(params: String): Either<Failure, Body> {
        return resultRepository.results(params)

    }

}