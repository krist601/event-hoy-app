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
import kotlinx.datetime.LocalDateTime
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
    category: Int? = null,
    onEventClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<EventListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    viewModel.setEventType(type = eventFilter)
    viewModel.setCategory(category = category)
    viewModel.fetchEventList()

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            CustomTopAppBar(
                stringResource(Res.string.app_name),
                onBackClick = {
                    onBackClick()
                },
                onNotificationsClick = {},
                onCalendarClick = {}
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
                },
                onLocationClick = {},
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
                model = event.imageUrl,
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title.orEmpty(),
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
                        text = event.title.orEmpty(),
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
                        text = "",//event.availableDates.orEmpty().first().startDate+" "+event.availableDates.orEmpty().first().endDate,
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
            title = "Concierto de Rock Sinfónico",
            description = "Una experiencia única que mezcla el poder del rock con la majestuosidad de una orquesta sinfónica.",
            status = "active",
            ticketPrice = "25.000 CLP",
            imageUrl = "https://example.com/images/rock-sinfonico.jpg",
            createdAt = LocalDateTime.parse("2025-08-01T12:00:00"),
            updatedAt = LocalDateTime.parse("2025-08-05T14:30:00"),
            venue = EventEntity.VenueEntity(
                id = 101,
                name = "Teatro Municipal de Santiago",
                address = "Agustinas 794",
                latitude = -30.01,
                longitude = -30.01,
                url = ""
            ),
            category = EventEntity.CategoryEntity(
                id = 5,
                name = "Música"
            ),
            availableDates = listOf(
                EventEntity.AvailableDatesEntity(
                    id = 1001,
                    startDate = "2025-08-10T20:10:00",
                    endDate = "2025-08-10T22:30:00"
                ),
                EventEntity.AvailableDatesEntity(
                    id = 1002,
                    startDate = "2025-08-11T20:00:00",
                    endDate = "2025-08-11T22:30:00"
                )
            ),
            totalDates = 2,
            nextDate = "2025-08-10T20:00:00",
            recurrenceInfo = EventEntity.RecurrenceInfoEntity(
                recurrenceType = "daily",
                interval = 1,
                startDate = "2025-08-10T20:00:00",
                endDate = "2025-08-11T22:30:00"
            )
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