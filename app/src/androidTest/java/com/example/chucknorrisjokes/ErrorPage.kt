package com.example.chucknorrisjokes

import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

class ErrorPage(private val message: String) : AbstractPage() {
    fun checkVisible() {
        titleTextView.check(matches(isDisplayed()))

        mainTextView
            .check(isCompletelyBelow(R.id.titleTextView))
            .check(matches(TextColorMatcher(R.color.orange)))
            .check(matches(withText(message)))

        jokeButton.check(isCompletelyBelow(R.id.mainTextView))
    }
}
