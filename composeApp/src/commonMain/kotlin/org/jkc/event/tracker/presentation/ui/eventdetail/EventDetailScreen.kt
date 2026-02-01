package org.jkc.event.tracker.presentation.ui.eventdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import eventtracker.composeapp.generated.resources.Res
import eventtracker.composeapp.generated.resources.back_content_description
import eventtracker.composeapp.generated.resources.share_content_description
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jkc.event.tracker.domain.entity.EventEntity
import org.jkc.event.tracker.presentation.ui.common.ErrorContent
import org.jkc.event.tracker.presentation.ui.common.LoadingContent
import org.jkc.event.tracker.presentation.util.uistates.EventUIState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventDetailRoute(
    eventId: Int,
    onShareClick: (String) -> Unit,
    onBackClick: () -> Unit
){
    val viewModel = koinViewModel<EventDetailViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchEvent(eventId)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            when (val currentState = state) {
                is EventViewState.Loading -> {
                    LoadingContent()
                }

                is EventViewState.Success -> {
                    EventDetailScreen(
                        data = EventUIState(
                            event = currentState.event
                        ),
                        onShareClick = onShareClick,
                        onBackClick = onBackClick
                    )
                }

                is EventViewState.Error -> {
                    ErrorContent(
                        errorType = currentState.errorType,
                        onRetryClick = { viewModel.fetchEvent(eventId) }
                    )
                }
            }
        }
    }
}
@Composable
fun EventDetailScreen(
    data: EventUIState,
    onShareClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = data.event.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )

            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back_content_description),
                    tint = Color.White
                )
            }
            val shareButton: () -> Unit = {
                onShareClick.invoke("http://eventhoy.cl/event/"+data.event)
            }

            IconButton(
                onClick = shareButton,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = stringResource(Res.string.share_content_description),
                    tint = Color.White
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {

            Text(
                text = data.event.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = data.event.status.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                /*data.event.endDate?.let { LocalDateTime.parse(it) }?.let {
                    Text(
                        text = it.format(
                                LocalDateTime.Format {
                                    year()
                                    char('-')
                                    monthNumber()
                                    char('-')
                                    dayOfMonth()

                                    chars(" at ")

                                    hour()
                                    char(':')
                                    minute()
                                }),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }*/
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = data.event.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )

            TextButton(onClick = {
                //onReadMoreClick(article.url)
            }, modifier = Modifier.fillMaxWidth()) {
                //Text(text = stringResource(Res.string.read_more_at, article.newsSite))
            }
        }
    }
}

private val data = EventUIState(
    event = EventEntity(
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
                startDate = "2025-08-10T20:00:00",
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

@Preview
@Composable
fun EventDetailPreview() {
    EventDetailScreen(
        data, {}, {}
    )
}