package com.example.chucknorrisjokes.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface Representative<T : Any> {

    fun startGettingUpdates(observer: UiObserver<T>)

    fun stopGettingUpdates(observer: UiObserver<T> = UiObserver.Empty())

    abstract class Abstract<R : Any>(private val runAsync: RunAsync) : Representative<R> {

        private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        protected fun <T : Any> handleAsync(
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            runAsync.runAsync(coroutineScope, backgroundBlock, uiBlock)
        }

        protected fun clear() = runAsync.clear()
    }
}