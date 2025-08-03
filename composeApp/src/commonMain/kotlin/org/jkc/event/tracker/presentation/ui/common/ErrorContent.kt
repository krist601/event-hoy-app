package org.jkc.event.tracker.presentation.ui.common

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eventtracker.composeapp.generated.resources.Res
import eventtracker.composeapp.generated.resources.no_internet
import eventtracker.composeapp.generated.resources.retry
import eventtracker.composeapp.generated.resources.server_error
import eventtracker.composeapp.generated.resources.something_went_wrong
import org.jetbrains.compose.resources.stringResource

@Composable
fun BoxScope.ErrorContent(
    errorType: ErrorType,
    onRetryClick: () -> Unit
) {
    val errorMessage = when (errorType) {
        ErrorType.NoInternet -> stringResource(Res.string.no_internet)
        ErrorType.ServerError -> stringResource(Res.string.server_error)
        ErrorType.Unknown -> stringResource(Res.string.something_went_wrong)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.Center)
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRetryClick) {
            Text(text = stringResource(Res.string.retry))
        }
    }
}



sealed class ErrorType {
    data object NoInternet : ErrorType()
    data object ServerError : ErrorType()
    data object Unknown : ErrorType()
}