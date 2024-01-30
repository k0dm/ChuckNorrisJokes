package com.example.chucknorrisjokes.presentation

import com.example.chucknorrisjokes.core.UiObservable

interface UiStateObservable: UiObservable<UiState>{
    object Base: UiStateObservable, UiObservable.Abstract<UiState>(UiState.Empty)
}