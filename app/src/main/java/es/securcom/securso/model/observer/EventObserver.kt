package es.securcom.securso.model.observer

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class EventObserver @Inject constructor() {
    var value: String? = null
    var observableLocation: Subject<String> = PublishSubject.create()
}