package com.vanuvakta.myapplication


import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.vanuvakta.myapplication.api.ServiceBuilder
import com.vanuvakta.myapplication.ui.AddCompanyActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class AddCompanyTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(AddCompanyActivity::class.java)

    @Test
    fun testAddProductUI(){
        ServiceBuilder.token="Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNzJiOTI1ZTE1N2ZlMGE1NDA0YTk4ZCIsImlhdCI6MTYxODYwMjI2NywiZXhwIjoxNjIxMTk0MjY3fQ.wRipXR6jrYt-HSqJOON2O9TfjXHochHWteT1Br9Ru9U"
        //make sure account type of the token is owner

        onView(withId(R.id.companyName)).perform((typeText("ANDROID")))
        onView(withId(R.id.phoneNumber)).perform((typeText("1111111111")))
        onView(withId(R.id.email)).perform((typeText("hero@gmail.com")))
        onView(withId(R.id.address)).perform((typeText("Maitighar, Nepal")))
        closeSoftKeyboard()
        onView(withId(R.id.btn_add_company)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btn_add))
                .check(matches(isDisplayed()))
    }
}