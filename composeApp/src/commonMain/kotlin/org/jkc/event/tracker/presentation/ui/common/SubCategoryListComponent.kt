package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jkc.event.tracker.domain.entity.SubCategoryEntity

@Composable
fun SubCategoryList(
    title: String,
    modifier: Modifier = Modifier,
    subCategoryList: List<SubCategoryEntity>,
    onCategoryEventsClick: (SubCategoryEntity) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(subCategoryList) {
        val index = subCategoryList.indexOfFirst { it.isSelected }
        if (index >= 0) {
            listState.animateScrollToItem(index)
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            text = "$title - Sub Categorías",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(subCategoryList) { index, subCategory ->

                SubCategoryCard(
                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                    subCategory = subCategory,
                    onCategoryEventsClick = { onCategoryEventsClick.invoke(subCategory) }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SubCategoryCard(
    subCategory: SubCategoryEntity,
    modifier: Modifier = Modifier,
    onCategoryEventsClick: (Int) -> Unit
) {
    val isSelected = subCategory.isSelected
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
    val border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.primary)

    Card(
        modifier = modifier
            .clickable { onCategoryEventsClick(subCategory.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp)
        ) {
            Text(
                text = subCategory.name,
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}
