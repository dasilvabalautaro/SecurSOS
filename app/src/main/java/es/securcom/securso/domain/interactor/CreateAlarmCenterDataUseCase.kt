package es.securcom.securso.domain.interactor

import es.securcom.securso.domain.functional.Either
import es.securcom.securso.model.exception.Failure
import es.securcom.securso.model.persistent.database.entity.AlarmCenterData
import es.securcom.securso.model.persistent.database.interfaces.AlarmCenterDataDao
import es.securcom.securso.presentation.data.AlarmCenterView
import javax.inject.Inject

class CreateAlarmCenterDataUseCase @Inject constructor(private val alarmCenterDataDao:
                                                       AlarmCenterDataDao):
    UseCase<Boolean, AlarmCenterView>() {

    override suspend fun run(params: AlarmCenterView): Either<Failure, Boolean> {
        return try {
            val alarmCenterData = AlarmCenterData(params.id, params.name?:"",
                params.phone?:"", params.email?:"", params.logo?:"",
                params.color?:"", params.web?:"", params.ipPrimary?:"",
                params.portPrimary?:"", params.ipSecondary?:"",
                params.portSecondary?:"", params.created?:"",
                params.updated?:"", params.information?:"",
                params.lapseLocation, params.latLngType, params.lowBatteryAlert,
                params.lowBatteryAlertValue, params.lowBatteryAlarm,
                params.lowBatteryAlarmValue, params.lowSignalAlert,
                params.lowSignalAlertValue, params.line?:"", params.active,
                params.devMax?:"", params.updateTime, params.reportInitApp,
                params.reportCloseApp)

            alarmCenterDataDao.insert(alarmCenterData)
            Either.Right(true)
        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }
    }

}