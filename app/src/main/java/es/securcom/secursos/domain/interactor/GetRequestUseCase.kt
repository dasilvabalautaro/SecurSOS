package es.securcom.secursos.domain.interactor

import es.securcom.secursos.domain.entity.Body
import es.securcom.secursos.domain.functional.Either
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.network.ResultRepository
import javax.inject.Inject

class GetRequestUseCase @Inject constructor(private val resultRepository:
                                                ResultRepository):
    UseCase<Body, String>() {

    override suspend fun run(params: String): Either<Failure, Body> {
        return resultRepository.results(params)

    }

}