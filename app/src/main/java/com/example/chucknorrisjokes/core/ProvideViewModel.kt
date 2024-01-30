package com.example.chucknorrisjokes.core

import com.example.chucknorrisjokes.BuildConfig
import com.example.chucknorrisjokes.data.ChuckNorrisJokesService
import com.example.chucknorrisjokes.data.JokesRepository
import com.example.chucknorrisjokes.presentation.MainViewModel
import com.example.chucknorrisjokes.presentation.UiStateObservable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ProvideViewModel {

    fun <T : Representative<*>> viewModel(clazz: Class<out T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory() : ProvideViewModel {

        private val viewModelMap = HashMap<Class<out Representative<*>>, Representative<*>>()

        private val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        private val isRelease = BuildConfig.DEBUG
        private val repository = if (isRelease) {
            JokesRepository.Base(retrofit.create(ChuckNorrisJokesService::class.java))
        } else {
            JokesRepository.Fake(isSuccessResponse = true)
        }

        override fun <T : Representative<*>> viewModel(clazz: Class<out T>): T {
            return if (viewModelMap.contains(clazz)) {
                viewModelMap[clazz]
            } else {
                val viewModel = when (clazz) {
                    MainViewModel::class.java -> MainViewModel.Base(
                        RunAsync.Base(),
                        UiStateObservable.Base,
                        repository
                    )

                    else -> throw IllegalStateException("No such viewModel with class: $clazz")
                }
                viewModelMap[clazz] = viewModel
                viewModel
            } as T
        }
    }
}