package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SearchBarComponent(
    query: String,
    onQueryChanged: (String) -> Unit,
    onLocationClick: () -> Unit,
    selectedLocation: String? = null,
    modifier: Modifier = Modifier
) {
    val lightGray = Color(0xFFFAFAFA)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = { Text("Search Event") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (query.isNotEmpty()) {
                        androidx.compose.material3.IconButton(onClick = { onQueryChanged("") }) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                    androidx.compose.material3.IconButton(
                        onClick = onLocationClick,
                        colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White,
                            contentColor = if (selectedLocation != null) Color.Red else Color.Gray
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Location"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightGray,
                unfocusedContainerColor = lightGray,
                disabledContainerColor = lightGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )
        if (selectedLocation != null) {
            Text(
                text = selectedLocation,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}