package com.example.chucknorrisjokes.domain

import com.example.chucknorrisjokes.presentation.UiMapper

interface LoadResult {

    fun map(mapper: UiMapper)

    data class Success(private val joke: String) : LoadResult {
        override fun map(mapper: UiMapper) = mapper.mapSuccess(joke)
    }

    data class Error(private val message: String) : LoadResult {
        override fun map(mapper: UiMapper) = mapper.mapError(message)
    }
}