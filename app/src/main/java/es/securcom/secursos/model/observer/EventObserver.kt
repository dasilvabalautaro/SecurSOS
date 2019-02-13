package es.securcom.secursos.model.observer

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

class EventObserver @Inject constructor() {
    var value: String? = null
    var observableLocation: Subject<String> = PublishSubject.create()
}