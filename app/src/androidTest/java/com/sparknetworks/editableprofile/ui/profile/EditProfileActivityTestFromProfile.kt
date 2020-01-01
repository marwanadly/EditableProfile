package com.sparknetworks.editableprofile.ui.profile

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.sparknetworks.editableprofile.Helper
import com.sparknetworks.editableprofile.R
import com.sparknetworks.editableprofile.utli.Common
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class EditProfileActivityTestFromProfile{
    @Rule
    @JvmField
    var activityRule = object :ActivityTestRule<EditProfileActivity>(EditProfileActivity::class.java){
        override fun getActivityIntent(): Intent {
            val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
            val result = Intent(targetContext, EditProfileActivity::class.java)
            result.putExtra(Common.ACTIVITY_NAME_EXTRA, Common.PROFILE_ACTIVITY_EXTRA)
            return result
        }
    }

    private fun getResourceString(id: Int): String {
        val targetContext: Context =
            InstrumentationRegistry.getInstrumentation().targetContext
        return targetContext.resources.getString(id)
    }

    @Test
    fun test_height_field_invisible() {
        onView(withId(R.id.edit_profile_container)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_height)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun test_mandatory_fields_edit_profile() {
        onView(withId(R.id.profile_save_btn)).perform(click())
        onView(withId(R.id.text_input_display_name)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
        onView(withId(R.id.text_input_real_name)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
        onView(withId(R.id.text_input_birthday)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
        onView(withId(R.id.text_input_gender)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
        onView(withId(R.id.text_input_marital_status)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
        onView(withId(R.id.text_input_location)).check(matches(Helper.hasTextInputLayoutError(getResourceString(R.string.required))))
    }
}