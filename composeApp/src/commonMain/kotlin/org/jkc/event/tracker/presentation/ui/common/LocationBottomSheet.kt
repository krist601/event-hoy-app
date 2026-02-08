package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jkc.event.tracker.domain.entity.LocationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    locationList: List<LocationEntity>,
    onLocationSelected: (LocationEntity?) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            item {
                ListItem(
                    headlineContent = { Text("Mi Localización") },
                    modifier = Modifier.clickable {
                        onLocationSelected(LocationEntity(id = -1, name = "Mi Ubicación", address = null, latitude = 0.0, longitude = 0.0, url = null)) // Special entity for my location
                    },
                    leadingContent = {
                        //Icon(Icons.Default.Place, contentDescription = null)
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text("Todos") },
                    modifier = Modifier.clickable {
                        onLocationSelected(null)
                    },
                    leadingContent = {
                        //Icon(Icons.Default.Place, contentDescription = null)
                    }
                )
            }
            items(locationList) { location ->
                ListItem(
                    headlineContent = { Text(location.name) },
                    modifier = Modifier.clickable {
                        onLocationSelected(location)
                    },
                    leadingContent = {
                        //Icon(Icons.Default.Place, contentDescription = null)
                    }
                )
            }
        }
    }
}
