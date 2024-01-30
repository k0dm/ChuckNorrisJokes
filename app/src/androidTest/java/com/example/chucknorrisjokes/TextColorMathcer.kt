package com.example.chucknorrisjokes

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class TextColorMatcher(private val color: Int) :
    BoundedMatcher<View, TextView>(TextView::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("color for text")
    }

    override fun matchesSafely(item: TextView) = item.currentTextColor == color
}