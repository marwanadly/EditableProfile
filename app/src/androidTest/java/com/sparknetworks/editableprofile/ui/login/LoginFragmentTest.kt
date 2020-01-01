package com.sparknetworks.editableprofile.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.Helper
import com.sparknetworks.editableprofile.ToastMatcher
import com.sparknetworks.editableprofile.ui.MainActivity
import com.sparknetworks.editableprofile.utli.Common
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    private val wrongEmailError =
        "There is no user record corresponding to this identifier. The user may have been deleted."
    private val wrongPasswordError = "The password is invalid or the user does not have a password."


    @Test
    fun test_login_success() {
        onView(withId(R.id.login_container)).check(matches(isDisplayed()))
        onView(withId(R.id.login_email)).perform(typeText("marwanadly112@gmail.com"))
        onView(withId(R.id.login_password)).perform(typeText("123123"))
        onView(withId(R.id.login_btn)).perform(click())
        onView(withId(R.id.login_loading)).check(matches(isDisplayed()))
    }

    @Test
    fun test_login_with_wrong_email() {
        onView(withId(R.id.login_container)).check(matches(isDisplayed()))
        onView(withId(R.id.login_email)).perform(typeText("marwanadly112@gmail.comm"))
        onView(withId(R.id.login_password)).perform(typeText("123123"))
        onView(withId(R.id.login_btn)).perform(click())
        onView(withId(R.id.login_loading)).check(matches(isDisplayed()))
        onView(withText(wrongEmailError))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
        onView(withId(R.id.login_loading)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_login_with_wrong_password() {
        onView(withId(R.id.login_container)).check(matches(isDisplayed()))
        onView(withId(R.id.login_email)).perform(typeText("marwan.elgaafary@gmail.com"))
        onView(withId(R.id.login_password)).perform(typeText("1231231"))
        onView(withId(R.id.login_btn)).perform(click())
        onView(withId(R.id.login_loading)).check(matches(isDisplayed()))
        onView(withText(wrongPasswordError)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withId(R.id.login_loading)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_validation_login_form() {
        onView(withId(R.id.login_container)).check(matches(isDisplayed()))
        onView(withId(R.id.login_btn)).perform(click())
        onView(withId(R.id.text_input_password)).check(matches(Helper.hasTextInputLayoutError(Common.PASSWORD_ERROR)))
        onView(withId(R.id.text_input_email)).check(matches(Helper.hasTextInputLayoutError(Common.EMAIL_ERROR)))
    }

    @Test
    fun test_navigate_to_register_page() {
        onView(withId(R.id.login_container)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container)).check(matches(isDisplayed()))
    }
}