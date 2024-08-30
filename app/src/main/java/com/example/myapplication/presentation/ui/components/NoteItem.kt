package com.example.myapplication.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.myapplication.R
import com.example.myapplication.domain.model.Note
import kotlinx.coroutines.delay

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun NoteItem(note: Note, onNoteClick: (Note) -> Unit, onSwipeLeft: (Note) -> Unit, onSwipeRight: (Note) -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)

    val swipeLeftColor = Color(0xFF81C784)
    val swipeRightColor = Color(0xFFE57373)

    val backgroundColor by animateColorAsState(
        when {
            swipeableState.offset.value < 0 -> swipeLeftColor.copy(alpha = (-swipeableState.offset.value / 150f).coerceIn(0f, 1f))
            swipeableState.offset.value > 0 -> swipeRightColor.copy(alpha = (swipeableState.offset.value / 150f).coerceIn(0f, 1f))
            else -> MaterialTheme.colorScheme.surface
        }, label = ""
    )

    val offsetX by animateFloatAsState(
        swipeableState.offset.value, label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onNoteClick(note)
            }
    ) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 4.dp
        ) {
            Card(
                modifier = Modifier
                    .swipeable(
                        state = swipeableState,
                        anchors = mapOf(
                            0f to 0,
                            -150f to -1,
                            150f to 1
                        ),
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .fillMaxWidth()
                    .padding(10.dp)
                    .offset(x = offsetX.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = note.textNote,
                        style = MaterialTheme.typography.displaySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = note.description.ifBlank { stringResource(R.string.no_description) },
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.done_label),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Checkbox(
                            checked = note.isDone,
                            onCheckedChange = null,
                            enabled = true,
                            colors = CheckboxDefaults.colors()
                        )
                    }
                }
            }
        }

        if (swipeableState.offset.value < -100f) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Mark as Done",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(24.dp),
                tint = Color.White
            )
        } else if (swipeableState.offset.value > 100f) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(24.dp),
                tint = Color.White
            )
        }
    }

    LaunchedEffect(swipeableState.currentValue) {
        println("SwipeableState: ${swipeableState.currentValue}")
        when (swipeableState.currentValue) {
            -1 -> onSwipeLeft(note)
            1 -> onSwipeRight(note)
        }
        delay(300)
        swipeableState.snapTo(0)
    }
}