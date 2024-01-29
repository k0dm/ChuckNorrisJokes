package com.example.chucknorrisjokes.core

import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : Representative<*>> viewModel(clazz: Class<out T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory() : ProvideViewModel {

        private val viewModelMap = HashMap<Class<out Representative<*>>, Representative<*>>()
        override fun <T> viewModel(clazz: Class<out T>): T {
            return if (viewModelMap.contains(clazz)) {
                viewModelMap[clazz]
            } else {
                val viewModel = when(clazz) {
                    MainViewModel::class.java -> MainViewModel.Base()

                    else -> throw IllegalStateException("No such viewModel with class: $clazz")
                }
                viewModel[clazz] = viewModel
                viewModel
            } as T
        }
    }

}