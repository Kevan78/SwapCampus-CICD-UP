package com.example.swapcampus_geoffroykevan.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.datetime.LocalDate
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
actual fun currentLanguageTag(): String =
    LocalConfiguration.current.locales[0].toLanguageTag()

actual fun formatPrice(price: Double, languageTag: String): String {
    val locale = Locale.forLanguageTag(languageTag)
    val format = NumberFormat.getCurrencyInstance(locale)
    format.currency = Currency.getInstance("EUR")
    return format.format(price)
}

actual fun formatDate(date: LocalDate, languageTag: String): String {
    val locale = Locale.forLanguageTag(languageTag)
    val formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
    val javaDate = java.util.GregorianCalendar(date.year, date.monthNumber - 1, date.dayOfMonth).time
    return formatter.format(javaDate)
}
