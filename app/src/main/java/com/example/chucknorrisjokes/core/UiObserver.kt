package com.example.chucknorrisjokes.core

interface UiObserver<T>{
    fun update(data: T)
}