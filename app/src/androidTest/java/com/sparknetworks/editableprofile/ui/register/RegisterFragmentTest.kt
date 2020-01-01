package com.sparknetworks.editableprofile.ui.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sparknetworks.editableprofile.Helper
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.ToastMatcher
import com.sparknetworks.editableprofile.ui.MainActivity
import com.sparknetworks.editableprofile.utli.Common
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    private val existedEmailError = "The email address is already in use by another account."

    @Test
    fun test_navigate_to_back() {
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container))
            .check(matches(isDisplayed()))
        onView(withId(R.id.back_btn)).perform(click())
        onView(withId(R.id.login_container))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_user_registration_success() {
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container)).check(matches(isDisplayed()))
        onView(withId(R.id.register_email)).perform(typeText(Helper.getRandomEmail()))
        onView(withId(R.id.register_password)).perform(typeText("123123"))
        onView(withId(R.id.register_confirm_password)).perform(typeText("123123"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_loading)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun test_user_registration_with_existed_email() {
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container)).check(matches(isDisplayed()))
        onView(withId(R.id.register_email)).perform(typeText("marwan.elgaafary@gmail.com"))
        onView(withId(R.id.register_password)).perform(typeText("123123"))
        onView(withId(R.id.register_confirm_password)).perform(typeText("123123"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.register_loading)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText(existedEmailError)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withId(R.id.register_loading)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_confirm_password_not_match_password() {
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container)).check(matches(isDisplayed()))
        onView(withId(R.id.register_email)).perform(typeText("marwan.elgaafary@gmail.com"))
        onView(withId(R.id.register_password)).perform(typeText("123123"))
        onView(withId(R.id.register_confirm_password)).perform(typeText("1231231"))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.text_input_confirm_password)).check(matches(Helper.hasTextInputLayoutError(Common.CONFIRM_PASSWORD_ERROR)))
    }

    @Test
    fun test_validation_registration_form() {
        onView(withId(R.id.nav_register_bt)).perform(click())
        onView(withId(R.id.register_container)).check(matches(isDisplayed()))
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.text_input_password)).check(matches(Helper.hasTextInputLayoutError(Common.PASSWORD_ERROR)))
        onView(withId(R.id.text_input_email)).check(matches(Helper.hasTextInputLayoutError(Common.EMAIL_ERROR)))
    }
}