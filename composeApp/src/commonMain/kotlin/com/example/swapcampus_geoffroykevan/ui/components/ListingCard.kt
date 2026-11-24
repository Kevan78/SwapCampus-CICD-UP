package com.example.swapcampus_geoffroykevan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.swapcampus_geoffroykevan.domain.Listing
import com.example.swapcampus_geoffroykevan.ui.i18n.currentLanguageTag
import com.example.swapcampus_geoffroykevan.ui.i18n.description
import com.example.swapcampus_geoffroykevan.ui.i18n.displayName
import com.example.swapcampus_geoffroykevan.ui.i18n.formatPrice

@Composable
fun ListingCard(listing: Listing, onClick: () -> Unit, showStatus: Boolean = false) {
    val languageTag = currentLanguageTag()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = listing.title.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(listing.title, style = MaterialTheme.typography.titleMedium)
                Text(formatPrice(listing.price, languageTag), style = MaterialTheme.typography.bodyLarge)
                Text(listing.category.displayName(), style = MaterialTheme.typography.bodySmall)
                if (showStatus) {
                    Text(listing.status.description(languageTag), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
