package com.vanuvakta.myapplication

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.vanuvakta.myapplication.ui.LoginActivity
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.concurrent.thread

@LargeTest
@RunWith(JUnit4::class)
class LogInTest {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    //invalid credintials test
    @Test
    fun testInvalidLoginUI(){
        onView(withId(R.id.et_email))
            .perform((typeText("asdsd@gmail.com")))
        onView(withId(R.id.et_password))
            .perform((typeText("password")))

        closeSoftKeyboard()

        onView(withId(R.id.btn_login))
            .perform(click())

        onView(withId(R.id.invalid))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testValidLoginUI(){
        onView(withId(R.id.et_email))
                .perform((typeText("shiva1@gmail.com")))
        onView(withId(R.id.et_password))
                .perform((typeText("password")))

        closeSoftKeyboard()

        onView(withId(R.id.btn_login))
                .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.news_feed))
                .check(matches(isDisplayed()))
    }
}