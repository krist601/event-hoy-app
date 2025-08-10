package org.jkc.event.tracker.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import eventtracker.composeapp.generated.resources.Res
import eventtracker.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.presentation.ui.common.CalendarComponent
import org.jkc.event.tracker.presentation.ui.common.CategoryList
import org.jkc.event.tracker.presentation.ui.common.CustomTopAppBar
import org.jkc.event.tracker.presentation.ui.common.ErrorContent
import org.jkc.event.tracker.presentation.ui.common.LoadingContent
import org.jkc.event.tracker.presentation.ui.common.SearchBarComponent
import org.jkc.event.tracker.presentation.ui.eventlist.EventCard
import org.jkc.event.tracker.presentation.util.uistates.HomeUIState
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    onEventClick: (Int) -> Unit,
    onCategoryEventsClick: (Int) -> Unit,
    onUpcomingEventsClick: () -> Unit,
    onSuggestedEventsClick: () -> Unit,
){
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var selectedDate by remember {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            CustomTopAppBar(
                stringResource(Res.string.app_name),
                onBackClick = null,
                onNotificationsClick = null,
                onCalendarClick = { showSheet = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is HomeViewState.Loading -> {
                    LoadingContent()
                }

                is HomeViewState.Success -> {

                    Column {
                        SearchBarComponent(
                            query = searchQuery,
                            onQueryChanged = {
                                searchQuery = it
                                viewModel.resetPages()
                                viewModel.fetchEventList(searchQuery)
                            }
                        )
                        HomeScreen(
                            data = HomeUIState(
                                upcomingEventList = currentState.upcomingEventList,
                                suggestedEventList = currentState.suggestedEventList,
                                categoryList = currentState.categoryList
                            ),
                            onCategoryEventsClick = onCategoryEventsClick,
                            onEventClick = onEventClick,
                            onUpcomingEventsClick = onUpcomingEventsClick,
                            onSuggestedEventsClick = onSuggestedEventsClick,
                            onLoadNewPage = { viewModel.fetchNewPage() }
                        )
                    }

                }

                is HomeViewState.Error -> {
                    ErrorContent(
                        errorType = currentState.errorType,
                        onRetryClick = { viewModel.fetchEventList() }
                    )
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            CalendarComponent(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    showSheet = false
                }
            )
        }
    }
}
@Composable
fun UpcomingEventsRow(
    events: List<EventEntity>,
    modifier: Modifier = Modifier,
    onEventClick: (Int) -> Unit,
    onUpcomingEventsClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Proximos Eventos",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Todos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onUpcomingEventsClick.invoke() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            itemsIndexed(events) { index, event ->
                val startPadding = when (index) {
                    0 -> 16.dp
                    else -> 8.dp
                }
                val endPadding = when (index) {
                    events.lastIndex -> 16.dp
                    else -> 8.dp
                }
                LateralEventCard(
                    event = event,
                    modifier = Modifier.padding(start = startPadding, end = endPadding)){
                    onEventClick.invoke(event.id)
                }
            }
        }
    }
}

@Composable
fun LateralEventCard(
    event: EventEntity,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick(event.id) }
            .width(280.dp)
            .height(280.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = event.title.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.description.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "",//event.availableDates.orEmpty().first().startDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Starting at",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "1000",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    data: HomeUIState,
    onCategoryEventsClick: (Int) -> Unit,
    onEventClick: (Int) -> Unit,
    onUpcomingEventsClick: () -> Unit,
    onSuggestedEventsClick: () -> Unit,
    onLoadNewPage: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= data.suggestedEventList.lastIndex
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !data.isLoadingMore) {
            onLoadNewPage.invoke()
        }
    }
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(0.dp)
    ) {
        item {
            CategoryList(
                categoryList = data.categoryList,
                onCategoryEventsClick = onCategoryEventsClick
            )
        }

        item {
            UpcomingEventsRow(
                modifier = Modifier.padding(top = 16.dp),
                events = data.upcomingEventList,
                onEventClick = onEventClick,
                onUpcomingEventsClick = onUpcomingEventsClick
            )
        }

        item{
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sugeridos para ti",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Todos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSuggestedEventsClick.invoke() }
                )
            }
        }

        items(data.suggestedEventList) { event ->
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