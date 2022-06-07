package com.ryanmolyneux.letsgooocomposables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressBadge(currentProgress: Int,
                  contentColour: Color,
                  completionBadge: ImageVector,
                  progressBadgeSizeDp: Dp) {
    Box(modifier = Modifier.size(progressBadgeSizeDp)) {
        if (currentProgress == 100) {
            Image(completionBadge, null,
                Modifier
                    .size(progressBadgeSizeDp)
                    .clip(CircleShape))
        } else if (currentProgress >= 0 && currentProgress < 100) {
            CircularProgressIndicator(
                progress = (currentProgress.toFloat() / 100f),
                modifier = Modifier.size(progressBadgeSizeDp),
                color = contentColour
            )
            Text(
                text = "${currentProgress}",
                color = contentColour,
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
    ProgressBadge(50, Color(0xFFDDFD00), ImageVector.vectorResource(id = R.drawable.ic_launcher_background), 50.dp)
}


@Preview
@Composable
fun ProgressBadgePreviewComplete() {
    ProgressBadge(100, Color(0xFFDDFD00), ImageVector.vectorResource(id = R.drawable.ic_launcher_background), 50.dp)
}