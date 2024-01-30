package com.example.chucknorrisjokes.data

import retrofit2.http.GET

interface ChuckNorrisJokesService {

    @GET("/jokes/random")
    suspend fun randomJoke(): JokeResponse
}