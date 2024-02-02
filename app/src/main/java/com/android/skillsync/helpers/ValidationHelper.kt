package com.android.skillsync.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.android.skillsync.R
import com.android.skillsync.databinding.CustomInputFieldPasswordBinding
import com.android.skillsync.databinding.CustomInputFieldTextBinding

object ValidationHelper {

    fun handleValidationResult(
        isValid: Boolean,
        inputGroup: ViewBinding,
        context: Context
    ) {
        if (inputGroup is CustomInputFieldTextBinding) {
            if (isValid) {
                showValidInput(inputGroup, context)
            } else {
                showTextError(inputGroup, context)
            }
        } else if (inputGroup is CustomInputFieldPasswordBinding) {
            if (isValid) {
                showValidInput(inputGroup, context)
            } else {
                showTextError(inputGroup, context)
            }

        } else {
            throw IllegalArgumentException("Invalid inputGroup type")
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /*TODO - password validation*/
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidString(input: String): Boolean {
        // Add your generic string validation logic here
        val pattern = Regex("^[a-zA-Z,\\. ]+\$")
        val isLengthValid = input.length >= 3
        return pattern.matches(input) && isLengthValid && input.isNotBlank()

    }

    fun isValidAddress(input: Editable): Boolean {
        return input.isNotEmpty()
    }

    private fun showValidInput(
        inputGroup: CustomInputFieldTextBinding,
        context: Context
    ) {

        inputGroup.errorMessage.visibility = View.INVISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.Light_Gray
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.Dark_Gray
            )
        )
    }


    @SuppressLint("SetTextI18n")
    private fun showTextError(inputGroup: CustomInputFieldTextBinding, context: Context) {
        if (inputGroup.editTextLabel.text == context.getString(R.string.student_description_title)) {
            inputGroup.errorMessage.text = "Invalid group description"
        } else if (inputGroup.editTextLabel.text == context.getString(R.string.team_members_name_title)) {
            inputGroup.errorMessage.text = "Invalid group members name"
        } else {
            inputGroup.errorMessage.text = "Invalid ${inputGroup.editTextLabel.text}"
        }

        inputGroup.errorMessage.visibility = View.VISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.Dark_Gray
            )
        )
    }

    /*password handling*/
    private fun showValidInput(
        inputGroup: CustomInputFieldPasswordBinding,
        context: Context
    ) {

        inputGroup.errorMessage.visibility = View.INVISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.Light_Gray
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.Dark_Gray
            )
        )
    }


    @SuppressLint("SetTextI18n")
    private fun showTextError(inputGroup: CustomInputFieldPasswordBinding, context: Context) {
        if (inputGroup.editTextLabel.text == context.getString(R.string.student_description_title)) {
            inputGroup.errorMessage.text = "Invalid group description"
        } else if (inputGroup.editTextLabel.text == context.getString(R.string.team_members_name_title)) {
            inputGroup.errorMessage.text = "Invalid group members name"
        } else {
            inputGroup.errorMessage.text = "Invalid ${inputGroup.editTextLabel.text}"
        }

        inputGroup.errorMessage.visibility = View.VISIBLE
        inputGroup.editTextLine.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.red
            )
        )
        inputGroup.editTextLabel.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.Dark_Gray
            )
        )
    }

    /*address validation*/
    fun handleValidationResult(
        isValid: Boolean,
        addEditTextLine: View,
        inputSuggestions: TextView,
        context: Context,

        ) {
        if (!isValid) {
            addEditTextLine.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )
            inputSuggestions.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )
        } else {
            addEditTextLine.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.Light_Gray
                )
            )
            inputSuggestions.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.Dark_Gray
                )
            )
        }
    }
}
