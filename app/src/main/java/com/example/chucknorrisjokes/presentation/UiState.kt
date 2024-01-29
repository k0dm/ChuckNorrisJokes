package com.example.chucknorrisjokes.presentation

import com.example.chucknorrisjokes.databinding.ActivityMainBinding

interface UiState{
    fun show(binding: ActivityMainBinding)

    object Empty: UiState {
        override fun show(binding: ActivityMainBinding) = Unit
    }
}