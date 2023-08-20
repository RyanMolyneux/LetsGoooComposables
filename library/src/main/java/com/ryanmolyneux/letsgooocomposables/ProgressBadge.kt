package com.ryanmolyneux.letsgooocomposables

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ProgressBadge(
    currentProgress: Float,
    badgeColour: Color,
    badgeSizeDp: Dp,
    badgeCompletionIcon: ImageVector? = null,
    badgeCompletionIconSizeDp: Dp? = null,
    progressChangeAnimationDurationMillis: Int? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = progressChangeAnimationDurationMillis
                ?: DEFAULT_PROGRESS_DURATION_ANIMATION_MILLIS
        )
    )

    Surface(
        shape = CircleShape,
        color = MaterialTheme.colors.surface,
        elevation = 0.dp,
        modifier = Modifier.border(
            1.dp, color = MaterialTheme.colors.secondary, shape = CircleShape
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(badgeSizeDp)
        ) {
            if (animatedProgress >= 100 && badgeCompletionIcon != DEFAULT_NO_COMPLETION_BADGE) {
                Icon(
                    badgeCompletionIcon ?: DEFAULT_NO_COMPLETION_BADGE,
                    null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(badgeCompletionIconSizeDp ?: (badgeSizeDp * 0.5f))
                )
            } else if (animatedProgress >= 0 && animatedProgress <= 100) {
                CircularProgressIndicator(
                    progress = (animatedProgress / 100f),
                    modifier = Modifier.size(badgeSizeDp),
                    color = badgeColour
                )
                Text(
                    text = "${animatedProgress.toInt()}",
                    fontSize = TextUnit(badgeSizeDp.value * 0.5f, TextUnitType.Sp),
                    color = badgeColour,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private val DEFAULT_NO_COMPLETION_BADGE =
    ImageVector.Builder(name = "DEFAULT_NO_COMPLETION_BADGE", 0.dp, 0.dp, 0f, 0f).build()
private const val DEFAULT_PROGRESS_DURATION_ANIMATION_MILLIS = 1000

@Preview
@Composable
fun ProgressBadgePreviewInProgress() {
    var progress by remember { mutableStateOf(25f) }
    Column {
        ProgressBadge(
            progress,
            Color(0xFFDDFD00),
            25.dp,
            ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
            8.dp
        )
        Button(content = {
            Text("increment progress")
        }, onClick = {
            progress += 10
        })
        Button(content = {
            Text("decrement progress")
        }, onClick = {
            progress -= 10
        })
    }
}


@Preview
@Composable
fun ProgressBadgePreviewComplete() {
    ProgressBadge(
        100f,
        Color(0xFFDDFD00),
        50.dp,
        ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
        8.dp
    )
}