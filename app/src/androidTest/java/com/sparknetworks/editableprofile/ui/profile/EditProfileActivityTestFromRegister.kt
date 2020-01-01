package com.sparknetworks.editableprofile.ui.profile

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.utli.Common
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditProfileActivityTestFromRegister {

    @Rule
    @JvmField
    var activityRule = object :ActivityTestRule<EditProfileActivity>(EditProfileActivity::class.java){
        override fun getActivityIntent(): Intent {
            val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
            val result = Intent(targetContext, EditProfileActivity::class.java)
            result.putExtra(Common.ACTIVITY_NAME_EXTRA, Common.REGISTER_FRAGMENT_EXTRA)
            return result
        }
    }

    @Test
    fun test_alert_dialog_message() {
        onView(withText("Register Successfully")).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun test_height_field_invisible() {
        onView(withText(android.R.string.ok)).perform(click())
        onView(withId(R.id.edit_profile_container)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_height)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}