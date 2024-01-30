package com.example.chucknorrisjokes.presentation

import com.example.chucknorrisjokes.core.Representative
import com.example.chucknorrisjokes.core.RunAsync
import com.example.chucknorrisjokes.core.UiObserver
import com.example.chucknorrisjokes.data.JokesRepository

interface MainViewModel : Representative<UiState> {
    fun init(firstTime: Boolean)

    fun loadJoke()

    class Base(
        runAsync: RunAsync,
        private val observable: UiStateObservable,
        private val repository: JokesRepository,
        private val uiMapper: UiMapper = UiMapper.Base(observable)
    ): MainViewModel, Representative.Abstract<UiState>(runAsync){
        override fun init(firstTime: Boolean) {
            if (firstTime) observable.update(UiState.Initial())
        }

        override fun loadJoke() {
            observable.update(UiState.Loading)
            handleAsync({
                repository.joke()
            }){
                it.map(uiMapper)
            }
        }

        override fun startGettingUpdates(observer: UiObserver<UiState>) {
            observable.updateObserver(observer)
        }

        override fun stopGettingUpdates(observer: UiObserver<UiState>) {
            observable.updateObserver(observer)
        }
    }
}

interface UiMapper{
    fun mapSuccess(joke: String)

    fun mapError(message: String)

    class Base(private val observable: UiStateObservable): UiMapper{
        override fun mapSuccess(joke: String) {
            observable.update(UiState.Initial(joke))
        }

        override fun mapError(message: String) {
            observable.update(UiState.Error(message))
        }
    }
}