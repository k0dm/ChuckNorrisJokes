package com.example.chucknorrisjokes.core

import android.app.Application

class ChuckNorrisJokesApp : Application(), ProvideViewModel {

    private lateinit var factory: ProvideViewModel

    override fun onCreate() {
        super.onCreate()
        factory = ProvideViewModel.Factory()
    }

    override fun <T> viewModel(clazz: Class<out T>) = factory.viewModel(clazz)
}