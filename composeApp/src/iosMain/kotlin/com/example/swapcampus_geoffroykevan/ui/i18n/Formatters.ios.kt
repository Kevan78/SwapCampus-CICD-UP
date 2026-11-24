package com.example.swapcampus_geoffroykevan.ui.i18n

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSBundle

@Composable
actual fun currentLanguageTag(): String {
    val code = NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
    return when (code) {
        "fr" -> "fr-FR"
        "en" -> "en-US"
        else -> code?.let { if (it.contains("-")) it else "$it-${it.uppercase()}" } ?: "en-US"
    }
}

actual fun formatPrice(price: Double, languageTag: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale(languageTag)
        currencyCode = "EUR"
    }
    return formatter.stringFromNumber(NSNumber(price)) ?: "$price"
}

actual fun formatDate(date: LocalDate, languageTag: String): String {
    val components = NSDateComponents().apply {
        year = date.year.toLong()
        month = date.monthNumber.toLong()
        day = date.dayOfMonth.toLong()
    }
    val nsDate = NSCalendar.currentCalendar.dateFromComponents(components) ?: return date.toString()
    val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        locale = NSLocale(languageTag)
    }
    return formatter.stringFromDate(nsDate) ?: date.toString()
}
