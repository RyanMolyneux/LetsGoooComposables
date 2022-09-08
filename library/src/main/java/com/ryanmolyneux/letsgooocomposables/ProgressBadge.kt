package com.ryanmolyneux.letsgooocomposables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.*

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ProgressBadge(currentProgress: Float,
                  badgeColour: Color,
                  completionBadge: ImageVector,
                  badgeSizeDp: Dp,
                  progressChangeAnimationDurationMillis: Int = 1000) {
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = progressChangeAnimationDurationMillis
        )
    )

    Box(modifier = Modifier.size(badgeSizeDp)) {
        if (animatedProgress >= 100) {
            Image(completionBadge, null,
                Modifier
                    .size(badgeSizeDp)
                    .clip(CircleShape))
        } else if (animatedProgress >= 0 && animatedProgress < 100) {
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

@Preview
@Composable
fun ProgressBadgePreviewInProgress() {
    var progress by remember { mutableStateOf(25f) }
    Column {
        ProgressBadge(progress, Color(0xFFDDFD00), ImageVector.vectorResource(id = R.drawable.ic_launcher_background), 25.dp)
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
    ProgressBadge(100f, Color(0xFFDDFD00), ImageVector.vectorResource(id = R.drawable.ic_launcher_background), 50.dp)
}