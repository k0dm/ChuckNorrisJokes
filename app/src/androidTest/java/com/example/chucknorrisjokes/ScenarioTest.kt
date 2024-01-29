package com.example.chucknorrisjokes

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    private val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun successResponse(){
        var initialPage = InitialPage()
        initialPage.checkVisible()
        initialPage.clickGetJoke()

        initialPage = InitialPage("Chuck Norris is the world's most popular loner.")
        initialPage.checkVisible()
        initialPage.clickGetJoke()

        initialPage = InitialPage("The signs outside of Chuck Norris' properties all say \\\"TRESPASSERS WILL BE NORRISED\\\"")
        initialPage.checkVisible()
    }

    @Test
    fun failedResponse(){
        val initialPage = InitialPage()
        initialPage.checkVisible()
        initialPage.clickGetJoke()

        val errorPage = ErrorPage(message = "Server unavailable. Please try later.")
        errorPage.checkVisible()
        errorPage.clickGetJoke()
        errorPage.checkVisible()
    }
}