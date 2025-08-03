package org.jkc.event.tracker.presentation.ui.eventlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import eventtracker.composeapp.generated.resources.Res
import eventtracker.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.presentation.ui.common.CustomTopAppBar
import org.jkc.event.tracker.presentation.ui.common.ErrorContent
import org.jkc.event.tracker.presentation.ui.common.LoadingContent
import org.jkc.event.tracker.presentation.ui.common.SearchBarComponent
import org.jkc.event.tracker.presentation.util.uistates.EventListUIState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListRoute(
    eventFilter: String = "",
    onEventClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<EventListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    viewModel.setEventType(type = eventFilter)
    viewModel.fetchEventList()

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            CustomTopAppBar(
                stringResource(Res.string.app_name),
                onBackClick = {
                    onBackClick()
                },
                onNotificationsClick = {}
            )
        }
    ) { innerPadding ->
        Column{
            SearchBarComponent(
                query = searchQuery,
                onQueryChanged = {
                    searchQuery = it
                    viewModel.resetPages()
                    viewModel.fetchEventList(searchQuery)
                }
            )
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                when (val currentState = state) {
                    is EventListViewState.Loading -> {
                        LoadingContent(modifier = Modifier.padding(top = 64.dp))
                    }

                    is EventListViewState.Success -> {
                        EventListScreen(
                            data = EventListUIState(
                                currentState.eventList
                            ),
                            onEventClick = onEventClick,
                            onLoadNewPage = {
                                viewModel.fetchNewPage()
                            }
                        )
                    }

                    is EventListViewState.Error -> {
                        ErrorContent(
                            errorType = currentState.errorType,
                            onRetryClick = { viewModel.fetchEventList() }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun EventListScreen(
    data: EventListUIState,
    onEventClick: (Int) -> Unit,
    onLoadNewPage: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= data.eventList.lastIndex
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !data.isLoadingMore) {
            onLoadNewPage.invoke()
        }
    }

    LazyColumn(state = listState) {
        items(data.eventList) { event ->
            EventCard(event = event) {
                onEventClick.invoke(event.id)
            }
        }
        if (data.isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: EventEntity,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(event.id) }
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = event.image,
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge
                        .copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.startDate+" "+event.endDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

private val data = EventListUIState(
    eventList = listOf(
        EventEntity(
            id = 1,
            title = "Festival de Jazz de Santiago",
            description = "Un encuentro musical con los mejores exponentes del jazz nacional e internacional.",
            startDate = "2025-08-05T19:00:00Z",
            endDate = "2025-08-05T23:30:00Z",
            image = "https://example.com/images/jazzfest.jpg",
            externalUrl = "https://jazzfest.cl",
            source = "Municipalidad de Santiago",
            priceFrom = "10000",
            featured = true,
            ticketSaleStart = "2025-07-01T00:00:00Z",
            ticketSaleEnd = "2025-08-04T23:59:59Z",
            status = "confirmed",
            categoryId = 3,
            venueId = 12,
            createdAt = "2025-06-01T10:00:00Z",
            updatedAt = "2025-07-10T15:45:00Z"
        ),
        EventEntity(
            id = 2,
            title = "Exposición de Arte Contemporáneo",
            description = "Galería abierta con obras de artistas emergentes latinoamericanos.",
            startDate = "2025-08-15T10:00:00Z",
            endDate = "2025-08-30T18:00:00Z",
            image = "https://example.com/images/arte.jpg",
            externalUrl = null,
            source = "Museo de Arte Moderno",
            priceFrom = "0",
            featured = false,
            ticketSaleStart = null,
            ticketSaleEnd = null,
            status = "active",
            categoryId = 5,
            venueId = 8,
            createdAt = "2025-06-10T09:00:00Z",
            updatedAt = "2025-07-05T12:30:00Z"
        ),
        EventEntity(
            id = 3,
            title = "Concierto Sinfónico al Aire Libre",
            description = "La Orquesta Filarmónica interpreta clásicos en el Parque Metropolitano.",
            startDate = "2025-09-10T20:00:00Z",
            endDate = null,
            image = "https://example.com/images/sinfonico.jpg",
            externalUrl = "https://filarmonica.cl/eventos",
            source = "Filarmónica de Chile",
            priceFrom = "5000",
            featured = true,
            ticketSaleStart = "2025-08-01T00:00:00Z",
            ticketSaleEnd = "2025-09-09T23:59:59Z",
            status = "scheduled",
            categoryId = 2,
            venueId = 3,
            createdAt = "2025-06-15T11:00:00Z",
            updatedAt = "2025-07-12T16:10:00Z"
        )
    )
)

@Preview
@Composable
fun ScreenPreview(){
    EventListScreen(
        data,
        {}, {}
    )
}