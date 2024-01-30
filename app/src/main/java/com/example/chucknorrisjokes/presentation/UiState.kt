package com.example.chucknorrisjokes.presentation

import android.view.View
import com.example.chucknorrisjokes.R
import com.example.chucknorrisjokes.databinding.ActivityMainBinding

interface UiState {

    fun show(binding: ActivityMainBinding)

    data class Initial(private val joke: String = "") : UiState {
        override fun show(binding: ActivityMainBinding) = with(binding) {
            imageView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            loadingTextView.visibility = View.GONE
            mainTextView.visibility = View.VISIBLE
            mainTextView.text = joke
            mainTextView.setTextColor(mainTextView.context.getColor(R.color.black))
            jokeButton.visibility = View.VISIBLE
        }
    }

    object Loading : UiState {
        override fun show(binding: ActivityMainBinding) = with(binding) {
            imageView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            loadingTextView.visibility = View.VISIBLE
            mainTextView.visibility = View.GONE
            jokeButton.visibility = View.GONE
        }
    }


    data class Error(private val message: String) : UiState {
        override fun show(binding: ActivityMainBinding) = with(binding) {
            imageView.visibility = View.GONE
            progressBar.visibility = View.GONE
            loadingTextView.visibility = View.GONE
            mainTextView.visibility = View.VISIBLE
            mainTextView.text = message
            mainTextView.setTextColor(mainTextView.context.getColor(R.color.orange))
            jokeButton.visibility = View.VISIBLE
        }
    }

    object Empty : UiState {
        override fun show(binding: ActivityMainBinding) = Unit
    }
}