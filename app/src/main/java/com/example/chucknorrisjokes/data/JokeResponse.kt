package com.example.chucknorrisjokes.data

import com.example.chucknorrisjokes.domain.LoadResult
import com.google.gson.annotations.SerializedName

data class JokeResponse(
    @SerializedName("value")
    private val joke: String
) {
    fun toLoadResult() = LoadResult.Success(joke)
}