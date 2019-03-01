package es.securcom.securso.domain.interactor

import es.securcom.securso.domain.functional.Either
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.database.entity.EventualData
import es.securcom.securso.model.persistent.database.interfaces.EventualDataDao
import es.securcom.securso.presentation.data.EventualView
import javax.inject.Inject

class CreateEventualDataUseCase @Inject constructor(private val eventualDataDao:
                                                    EventualDataDao):
    UseCase<Boolean, EventualView>() {
    override suspend fun run(params: EventualView): Either<Failure, Boolean> {
        return try {
            val eventualData = EventualData(0, params.batteryLevel,
                params.speed, params.latitude, params.longitude,
                params.heading, params.lastUpdate)
            eventualDataDao.insert(eventualData)
            Either.Right(true)

        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }
    }


}