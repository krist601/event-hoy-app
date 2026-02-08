package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jkc.event.tracker.domain.entity.LocationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationList(
    modifier: Modifier = Modifier,
    locationList: List<LocationEntity>,
    onLocationEventsSelected: (LocationEntity) -> Unit
) {
    if (locationList.isEmpty()) return

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(locationList.first()) }

    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            text = "Location",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedOption.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona una opción") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                locationList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            selectedOption = option
                            onLocationEventsSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
