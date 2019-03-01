package es.securcom.securso.domain.interactor

import es.securcom.securso.domain.functional.Either
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.database.entity.SecurityOptionsData
import es.securcom.securso.model.persistent.database.interfaces.SecurityOptionsDataDao
import es.securcom.securso.presentation.data.SecurityOptionsView
import javax.inject.Inject

class CreateSecurityOptionsDataUseCase @Inject constructor(private val securityOptionsDataDao:
                                                           SecurityOptionsDataDao):
    UseCase<Boolean, SecurityOptionsView>() {

    override suspend fun run(params: SecurityOptionsView): Either<Failure, Boolean> {
        return try {
            val securityOptionsData = SecurityOptionsData(params.pk?:"",
                params.id, params.craId, params.evcode?:"",
                params.descr?:"", params.icon?:"",
                params.color?:"", params.time, params.count, params.audio,
                params.snap, params.alarmaretardada, params.notificarprealarma,
                params.tiempodefectodemoraminutos, params.hombremuerto,
                params.hombremuertovalor, params.calendariolunes,
                params.calendariomartes, params.calendariomiercoles,
                params.calendariojueves, params.calendarioviernes,
                params.calendariosabado, params.calendariodomingo,
                params.calendariohoraini1?:"", params.calendariohorafin1?:"",
                params.calendariohoraini2?:"", params.calendariohorafin2?:"")

            securityOptionsDataDao.insert(securityOptionsData)
            Either.Right(true)
        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }
    }

}