package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Card
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jkc.event.tracker.domain.entity.CategoryEntity

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryEntity>,
    onCategoryEventsClick: (CategoryEntity) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            text = "Categorías",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            itemsIndexed(categoryList) { index, category ->
                val startPadding = when (index) {
                    0 -> 8.dp
                    else -> 4.dp
                }
                val endPadding = when (index) {
                    categoryList.lastIndex -> 8.dp
                    else -> 4.dp
                }

                CategoryCard(
                    modifier = Modifier.padding(start = startPadding, end = endPadding),
                    category = category,
                    onCategoryEventsClick = { category ->
                        onCategoryEventsClick.invoke(category)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryEntity,
    modifier: Modifier = Modifier,
    onCategoryEventsClick: (CategoryEntity) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCategoryEventsClick(category) }
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = modifier
                .size(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Gray)
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            Box(
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = category.name,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}
