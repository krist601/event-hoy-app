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
                model = data.event.image,
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
                text = data.event.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = data.event.status,
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
                text = data.event.description,
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
    )
)

@Preview
@Composable
fun EventDetailPreview() {
    EventDetailScreen(
        data, {}, {}
    )
}