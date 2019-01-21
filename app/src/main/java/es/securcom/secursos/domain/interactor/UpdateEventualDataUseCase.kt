package es.securcom.secursos.domain.interactor

import es.securcom.secursos.domain.functional.Either
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.database.entity.EventualData
import es.securcom.secursos.model.persistent.database.interfaces.EventualDataDao
import es.securcom.secursos.presentation.data.EventualView
import javax.inject.Inject


class UpdateEventualDataUseCase @Inject constructor(private val eventualDataDao:
                                                    EventualDataDao):
    UseCase<Boolean, EventualView>(){
    override suspend fun run(params: EventualView): Either<Failure, Boolean> {

        return try {
            val eventualData = EventualData(params.id, params.batteryLevel,
                params.speed, params.latitude, params.longitude,
                params.heading, params.lastUpdate)
            eventualDataDao.update(eventualData)
            Either.Right(true)

        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

}