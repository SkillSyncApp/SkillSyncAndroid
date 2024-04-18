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
            if(inputGroup.editTextField.text.isNullOrEmpty()) {
                showTextError(inputGroup, context,"Required field")
            }
            else if (isValid) {
                showValidInput(inputGroup, context)
            } else {
                showTextError(inputGroup, context)
            }
        } else if (inputGroup is CustomInputFieldPasswordBinding) {
            if(inputGroup.editTextField.text.isNullOrEmpty()) {
                showTextError(inputGroup, context,"Required field")
            } else if (isValid) {
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
    private fun showTextError(inputGroup: CustomInputFieldTextBinding, context: Context,s:String? = null ) {
        if(s.isNullOrEmpty()) {
            when (inputGroup.editTextLabel.text) {
                        context.getString(R.string.email),context.getString(R.string.user_email_title) -> {
                    inputGroup.errorMessage.text = "Invalid Email Address"
                }
                context.getString(R.string.password) -> {
                    inputGroup.errorMessage.text = "Password must have at least 6 characters"
                }
                context.getString(R.string.bio_title),
                context.getString(R.string.company_name_title),
                context.getString(R.string.team_name_title),
                context.getString(R.string.team_institution_title)-> {
                    inputGroup.errorMessage.text = "Field must be at least 6 characters long and contain only letters" //, comma, dot, or space"
                }
                else -> {
                    inputGroup.errorMessage.text = "Invalid ${inputGroup.editTextLabel.text}"
                }
            }
        }
        else
            inputGroup.errorMessage.text = s;

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
    private fun showTextError(inputGroup: CustomInputFieldPasswordBinding, context: Context,s:String? = null) {
        if(s.isNullOrEmpty()) {
            inputGroup.errorMessage.text = "Password must have at least 6 characters"
        }
        else
            inputGroup.errorMessage.text = s;


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
