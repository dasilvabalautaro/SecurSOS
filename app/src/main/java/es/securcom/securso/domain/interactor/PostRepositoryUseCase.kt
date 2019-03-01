package es.securcom.securso.domain.interactor

import es.securcom.securso.domain.functional.Either
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.network.PostRepository
import es.securcom.securso.presentation.tools.Conversion
import javax.inject.Inject

class PostRepositoryUseCase @Inject constructor(private val postRepository:
                                                PostRepository):
    UseCase<String, List<String>>() {

    override suspend fun run(params: List<String>): Either<Failure, String> {
        val url = params[0]
        val body = Conversion.createRequestBody(params[1])
        return postRepository.results(url, body)
    }


}