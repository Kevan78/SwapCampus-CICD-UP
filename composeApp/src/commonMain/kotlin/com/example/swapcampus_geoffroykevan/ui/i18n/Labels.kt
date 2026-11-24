package com.example.swapcampus_geoffroykevan.ui.i18n

import androidx.compose.runtime.Composable
import com.example.swapcampus_geoffroykevan.domain.Category
import com.example.swapcampus_geoffroykevan.domain.ListingStatus
import com.example.swapcampus_geoffroykevan.resources.Res
import com.example.swapcampus_geoffroykevan.resources.category_books
import com.example.swapcampus_geoffroykevan.resources.category_clothing
import com.example.swapcampus_geoffroykevan.resources.category_courses
import com.example.swapcampus_geoffroykevan.resources.category_electronics
import com.example.swapcampus_geoffroykevan.resources.category_furniture
import com.example.swapcampus_geoffroykevan.resources.category_other
import com.example.swapcampus_geoffroykevan.resources.category_sports_leisure
import com.example.swapcampus_geoffroykevan.resources.error_buyer_empty
import com.example.swapcampus_geoffroykevan.resources.error_listing_not_found
import com.example.swapcampus_geoffroykevan.resources.error_price_number
import com.example.swapcampus_geoffroykevan.resources.error_price_positive
import com.example.swapcampus_geoffroykevan.resources.error_status_change
import com.example.swapcampus_geoffroykevan.resources.error_title_empty
import com.example.swapcampus_geoffroykevan.resources.status_available
import com.example.swapcampus_geoffroykevan.resources.status_reserved
import com.example.swapcampus_geoffroykevan.resources.status_sold
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

fun Category.label(): StringResource = when (this) {
    Category.BOOKS -> Res.string.category_books
    Category.COURSES -> Res.string.category_courses
    Category.ELECTRONICS -> Res.string.category_electronics
    Category.SPORTS_LEISURE -> Res.string.category_sports_leisure
    Category.FURNITURE -> Res.string.category_furniture
    Category.CLOTHING -> Res.string.category_clothing
    Category.OTHER -> Res.string.category_other
}

@Composable
fun Category.displayName(): String = stringResource(label())

@Composable
fun ListingStatus.description(languageTag: String): String = when (this) {
    is ListingStatus.Available -> stringResource(Res.string.status_available)
    is ListingStatus.Reserved -> stringResource(Res.string.status_reserved, buyer)
    is ListingStatus.Sold -> stringResource(Res.string.status_sold, formatDate(saleDate, languageTag))
}

fun repositoryErrorMessage(message: String?): StringResource? = when (message) {
    "The title cannot be empty", "Le titre ne peut pas être vide" -> Res.string.error_title_empty
    "The price must be greater than 0", "Le prix doit être supérieur à 0" -> Res.string.error_price_positive
    "The price must be a number", "Le prix doit être un nombre" -> Res.string.error_price_number
    "Listing not found", "Annonce introuvable" -> Res.string.error_listing_not_found
    "This status change is not allowed", "Ce changement de statut n'est pas autorisé" -> Res.string.error_status_change
    "The buyer name cannot be empty", "Le nom de l'acheteur ne peut pas être vide" -> Res.string.error_buyer_empty
    else -> null
}

@Composable
fun repositoryErrorText(message: String?): String? =
    repositoryErrorMessage(message)?.let { stringResource(it) } ?: message
