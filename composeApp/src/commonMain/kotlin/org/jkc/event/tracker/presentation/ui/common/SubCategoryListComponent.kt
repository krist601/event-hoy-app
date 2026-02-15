package org.jkc.event.tracker.presentation.ui.common

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
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            text = "$title - Sub Categorías",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            itemsIndexed(subCategoryList) { index, subCategory ->
                val startPadding = when (index) {
                    0 -> 8.dp
                    else -> 4.dp
                }
                val endPadding = when (index) {
                    subCategoryList.lastIndex -> 8.dp
                    else -> 4.dp
                }

                SubCategoryCard(
                    modifier = Modifier.padding(start = startPadding, end = endPadding),
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
    Card(
        modifier = modifier
            .clickable { onCategoryEventsClick(subCategory.id) }
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Gray)
        ) {
            /*AsyncImage(
            model = category.,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )*/
            Box(
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = subCategory.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}
