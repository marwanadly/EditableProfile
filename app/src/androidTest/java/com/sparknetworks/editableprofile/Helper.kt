package com.sparknetworks.editableprofile

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.*


class Helper {

    companion object {
        fun hasTextInputLayoutError(expectedErrorText: String): TypeSafeMatcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description?) {}
                override fun matchesSafely(item: View?): Boolean {
                    return if (item !is TextInputLayout) false else expectedErrorText == item.error.toString()
                }
            }
        }

        fun getRandomEmail(): String {
            val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
            val salt = StringBuilder()
            val rnd = Random()
            while (salt.length < 10) {
                val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
                salt.append(SALTCHARS[index])
            }
            return "$salt@gmail.com"
        }
    }
}