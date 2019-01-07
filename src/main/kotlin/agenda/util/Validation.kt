@file:JvmName("Validation")

package agenda.util

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.apache.commons.validator.routines.EmailValidator

const val NAME_LENGTH: Int = 100
const val PHONE_LENGTH: Int = 25
const val EMAIL_LENGTH: Int = 255

private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }
private val emailValidator: EmailValidator by lazy { EmailValidator.getInstance() }

fun String.isValidPhone(): Boolean = phoneUtil.runCatching { isValidNumber(parse(this@isValidPhone, "ES")) }.getOrDefault(false)
fun String.isValidEmail(): Boolean = emailValidator.isValid(this)
