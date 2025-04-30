package com.forknowledge.feature.authentication.extension

import android.util.Patterns

/**
 * Check if this Email is valid as Email format example@gmail.com.
 * @return true if this email is valid, false if otherwise.
 */
fun CharSequence.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Check if this Password is valid followed by conditions :
 * - Uppercase letters: A-Z
 * - Lowercase letters: a-z
 * - Numbers: 0-9
 * @return true if this email is valid, false if otherwise.
 */
fun CharSequence.isValidPassword() = this.run {
    length >= 8 && any {
        it.isLetter()
    } && any {
        it.isDigit()
    } && any {
        it.isUpperCase()
    } && any {
        it.isLowerCase()
    }
}
