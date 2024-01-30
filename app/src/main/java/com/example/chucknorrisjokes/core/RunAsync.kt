package com.example.chucknorrisjokes.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {

    fun <T : Any> runAsync(
        coroutineScope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    )

    fun clear()

    class Base(
        private val dispatcherMain: CoroutineDispatcher = Dispatchers.Main,
        private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
    ) : RunAsync {

        private var job: Job? = null
        override fun <T : Any> runAsync(
            coroutineScope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            job = coroutineScope.launch(dispatcherIO) {
                delay(1000)
                val result = backgroundBlock.invoke()
                withContext(dispatcherMain) {
                    uiBlock.invoke(result)
                }
            }
        }

        override fun clear() {
            job?.cancel()
            job = null
        }
    }
}