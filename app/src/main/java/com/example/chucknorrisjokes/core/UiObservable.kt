package com.example.chucknorrisjokes.core

interface UiObservable<T : Any> : UiObserver<T>, UpdateObserver<T> {

    fun clear()

    abstract class Abstract<T : Any>(private val empty: T) : UiObservable<T> {

        private var cache: T = empty
        private var observer: UiObserver<T> = UiObserver.Empty()

        override fun update(data: T) {
            cache = data
            observer.update(data)
        }

        override fun updateObserver(observer: UiObserver<T>) {
            this.observer = observer
            observer.update(cache)
        }

        override fun clear() {
            cache = empty
        }
    }
}


interface UiObserver<T : Any> {
    fun update(data: T)

    class Empty<T : Any> : UiObserver<T> {
        override fun update(data: T) = Unit
    }
}

interface UpdateObserver<T : Any> {
    fun updateObserver(observer: UiObserver<T>)
}
