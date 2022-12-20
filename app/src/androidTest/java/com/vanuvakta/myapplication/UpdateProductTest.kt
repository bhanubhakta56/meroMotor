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
import com.vanuvakta.myapplication.ui.UpdateProductActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@LargeTest
@RunWith(JUnit4::class)
class UpdateProductTest {
    @get:Rule
    val testRule = ActivityScenarioRule(UpdateProductActivity::class.java)

    @Test
    fun testUpdateProductUI(){
        ServiceBuilder.token="Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwNzMyNDRlOTJmYmU2MmRlMGQ4YzBjNSIsImlhdCI6MTYxODYwMDY3OCwiZXhwIjoxNjIxMTkyNjc4fQ.BsccGd6RAFKe3gnv08AIY6lWBJQ1MrDM5QXi5aoW_yQ"
        //make sure account type of the token is owner

//        val id ="607748c2dd1322313015d752"

        onView(withId(R.id.productName)).perform((typeText("Headlight")))
        onView(withId(R.id.price)).perform((typeText("234")))
        onView(withId(R.id.brand)).perform((typeText("Hero")))
        onView(withId(R.id.description)).perform((typeText("Hero is always Hero")))
        onView(withId(R.id.qty)).perform((typeText("12")))
        closeSoftKeyboard()
        onView(withId(R.id.btn_add)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.rc_view_product))
                .check(matches(isDisplayed()))

    }


}