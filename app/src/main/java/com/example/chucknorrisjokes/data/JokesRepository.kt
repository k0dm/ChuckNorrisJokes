package com.example.chucknorrisjokes.data

import com.example.chucknorrisjokes.domain.LoadResult
import java.net.UnknownHostException

interface JokesRepository {

    suspend fun joke(): LoadResult

    class Base(private val jokeService: ChuckNorrisJokesService) : JokesRepository {
        override suspend fun joke(): LoadResult {
            return try {
                jokeService.randomJoke().toLoadResult()
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    LoadResult.Error("No internet connection")
                } else {
                    LoadResult.Error("Service unavailable")
                }
            }
        }
    }

    class Fake(private val isSuccessResponse: Boolean) : JokesRepository {

        private val jokes = listOf(
            "Chuck Norris is the world's most popular loner.",
            "The signs outside of Chuck Norris' properties all say \"TRESPASSERS WILL BE NORRISED\""
        )
        private var index = 0

        override suspend fun joke(): LoadResult {
            return if (isSuccessResponse) {
                LoadResult.Success(joke = jokes[(index++) % jokes.size])
            } else {
                LoadResult.Error(message = "Server unavailable. Please try later.")
            }
        }
    }
}

