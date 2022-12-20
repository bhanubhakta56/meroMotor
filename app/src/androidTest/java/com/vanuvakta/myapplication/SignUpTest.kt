package com.vanuvakta.myapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.vanuvakta.myapplication.ui.LoginActivity
import com.vanuvakta.myapplication.ui.SignupActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class SignUpTest {
    @get:Rule
    val testRule = ActivityScenarioRule(SignupActivity::class.java)


    //invalid credintials test
    @Test
    fun testValidSignUpUI(){
        Espresso.onView(ViewMatchers.withId(R.id.et_first_name))
                .perform((ViewActions.typeText("bhanubhakta")))
        Espresso.onView(ViewMatchers.withId(R.id.et_last_name))
                .perform((ViewActions.typeText("bhandari")))
        Espresso.onView(ViewMatchers.withId(R.id.et_email))
                .perform((ViewActions.typeText("bhanubhakta@gmail.com")))
        Espresso.onView(ViewMatchers.withId(R.id.et_password)).perform((ViewActions.typeText("password")))
        Espresso.onView(ViewMatchers.withId(R.id.rd_male)).perform(click())

        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.btn_signup))
                .perform(click())

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.et_email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}