package es.securcom.secursos.domain.interactor

import es.securcom.secursos.domain.functional.Either
import es.securcom.secursos.model.exception.Failure
import es.securcom.secursos.model.persistent.database.entity.DeviceData
import es.securcom.secursos.model.persistent.database.interfaces.DeviceDataDao
import es.securcom.secursos.presentation.data.DeviceView
import javax.inject.Inject


class CreateDeviceDataUseCase @Inject constructor(private val deviceDataDao:
                                                  DeviceDataDao):
    UseCase<Boolean, DeviceView>() {

    override suspend fun run(params: DeviceView): Either<Failure, Boolean> {
        return try {
            val deviceData = DeviceData(params.id, params.phone?:"",
                params.fullName?:"", params.serviceNumber?:"",
                params.cra_id, params.created_at?:"",
                params.updated_at?:"", params.identifier?:"",
                params.button1, params.button2, params.button3, params.button4,
                params.active, params.lang?:"", params.lowBatteryAlert,
                params.lowBatteryAlertValue, params.lowBatteryAlarm,
                params.lowBatteryAlarmValue, params.lowSignalAlert,
                params.lowSignalAlertValue, params.reportInitApp, params.reportCloseApp)

            deviceDataDao.insert(deviceData)
            Either.Right(true)
        }catch (exception: Throwable){
            Either.Left(Failure.DatabaseError())
        }

    }

}