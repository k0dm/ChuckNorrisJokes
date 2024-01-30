package com.example.chucknorrisjokes

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf

abstract class AbstractPage() {

    protected val rootId = R.id.contentLayout

    protected val titleTextView = onView(
        allOf(
            withId(R.id.titleTextView),
            withText("Chuck Norris Jokes"),
            isAssignableFrom(TextView::class.java),
            withParent(withId(rootId)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )

    protected val mainTextView = onView(
        allOf(
            withId(R.id.mainTextView),
            isAssignableFrom(TextView::class.java),
            withParent(withId(rootId)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )

    protected val jokeButton = onView(
        allOf(
            withId(R.id.jokeButton),
            isAssignableFrom(Button::class.java),
            withText("Get Joke!"),
            withParent(withId(R.id.contentLayout)),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    )

    fun clickGetJoke() {
        jokeButton.perform(ViewActions.click())
    }
}