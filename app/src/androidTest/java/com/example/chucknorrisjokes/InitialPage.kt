package com.example.chucknorrisjokes

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf

class InitialPage(private val joke: String = "") : AbstractPage() {

    fun checkVisible() {

        titleTextView.check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.imageView),
                isAssignableFrom(ImageView::class.java),
                withParent(withId(rootId)),
                withParent(isAssignableFrom(LinearLayout::class.java)),
            )
        ).check(isCompletelyBelow(withId(R.id.titleTextView)))

        mainTextView
            .check(isCompletelyBelow(withId(R.id.imageView)))
            .check(matches(withText(joke)))

        jokeButton
            .check(isCompletelyBelow(withId(R.id.mainTextView)))
            .check(matches(TextColorMatcher(R.color.white)))

    }
}


