package com.ryanmolyneux.letsgooocomposables

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.AttributeSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


/**
 * Experimental stopwatch composable I've created for usage within applications
 * requiring a very basic stopwatch like any other, taking the following parameters
 * which are subject to name changes,
 *
 * @param name, the name you have given to your stopwatch which should uniquely identify it.
 *
 * @param objective, the reason why you have created your stopwatch/what is it you are trying to
 * achieve.
 *
 * @param initialLapState, the initial state of the current lap the stopwatch will have by default
 * a value of [LapState.Prepped] is passed here.
 *
 * @param initialPastLapStates, if you have recorded previous laps and wish for them to be shown
 * along with the state of the current lap you should pass them to this parameter here so they
 * can be shown and recorded in the results of the callback after the final lap.
 *
 * @param onCompleteCallback, the callback that you wish to be called once you have completed the
 * final lap on your stopwatch.
 *
 * note this along with alot of the other widgets in this library assumes the usage of MaterialTheme
 * stylings, so with that in mind stopwatch time uses h3, stopwatch days text uses h5,
 * stopwatch name, goal & lap number text use subtitle1.
 */
@Composable
fun Stopwatch(name: String,
              objective: String,
              initialLapState: LapState = LapState.Prepped,
              initialPastLapStates: List<LapState.Completed> = emptyList(),
              onCompleteCallback: (laps: List<LapState.Completed>) -> Unit = {}) {
    var currentLapState by remember { mutableStateOf(initialLapState) }
    val pastLapStates = remember { initialPastLapStates.toMutableStateList() }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        when (currentLapState) {
            is LapState.Prepped -> {
                Text(
                    text = "00:00:00.000",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "0 days",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is LapState.InProgress -> {
                var timeElapsed by remember {
                    mutableStateOf(TimeElapsed(0, 0, 0, 0, 0))
                }

                LaunchedEffect(key1 = currentLapState) {
                    while (true) {
                        delay(100L)
                        timeElapsed = getTimeBetween(
                            (currentLapState as LapState.InProgress).startTime,
                            Instant.now()
                        )
                    }
                }
                Text(
                    text = "${timeElapsed.hours.zeroPrefixedString()}:${timeElapsed.minutes.zeroPrefixedString()}:${timeElapsed.seconds.zeroPrefixedString()}.${
                        timeElapsed.milliseconds.zeroPrefixedString(
                            3
                        )
                    }",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${timeElapsed.days} days",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is LapState.Completed -> {
                val timeElapsed = getTimeBetween(
                    (currentLapState as LapState.Completed).startTime,
                    (currentLapState as LapState.Completed).endTime
                )

                Text(
                    text = "${timeElapsed.hours.zeroPrefixedString()}:${timeElapsed.minutes.zeroPrefixedString()}:${timeElapsed.seconds.zeroPrefixedString()}.${
                        timeElapsed.milliseconds.zeroPrefixedString(
                            3
                        )
                    }",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${timeElapsed.days} days",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 0.dp, end = 0.dp))
        Text(
            text = "Name: $name",
            style = MaterialTheme.typography.subtitle1,
        )
        Text(
            text = "Goal: $objective",
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 0.dp, end = 0.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = {
                if (currentLapState is LapState.InProgress) {
                    pastLapStates.add(
                        LapState.Completed(
                            startTime = (currentLapState as LapState.InProgress).startTime,
                            endTime = Instant.now()
                        )
                    )
                    currentLapState = LapState.InProgress(Instant.now())
                }
            }, enabled = ((currentLapState is LapState.Completed).not())) {
                Text("Lap")
            }
            Button(onClick = {
                if (currentLapState is LapState.Prepped) {
                    currentLapState = LapState.InProgress(Instant.parse("2007-12-03T10:15:30.00Z"))
                } else if (currentLapState is LapState.InProgress) {
                    val finalLapState = LapState.Completed(
                        startTime = (currentLapState as LapState.InProgress).startTime,
                        endTime = Instant.now()
                    )
                    currentLapState = finalLapState
                    pastLapStates.add(finalLapState)
                    onCompleteCallback(pastLapStates)
                }
            }, enabled = ((currentLapState is LapState.Completed).not())) {
                if (currentLapState is LapState.Completed) {
                    Text("Finished")
                } else if (currentLapState is LapState.InProgress) {
                    Text("Stop")
                } else {
                    Text("Start")
                }
            }
        }

        LazyColumn {
            itemsIndexed(pastLapStates) { index, lapCompletedState ->
                val lapTime = getTimeBetween(lapCompletedState.startTime, lapCompletedState.endTime)
                Spacer(modifier = Modifier.padding(top = 2.dp))
                Text(text = "#${(index + 1)}",
                    style = MaterialTheme.typography.subtitle1)
                Text(
                    text = "${lapTime.hours.zeroPrefixedString()}:${lapTime.minutes.zeroPrefixedString()}:${lapTime.seconds.zeroPrefixedString()}.${
                        lapTime.milliseconds.zeroPrefixedString(
                            3
                        )
                    }",
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "${lapTime.days} days",
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}


private fun getTimeBetween(startTime: Instant, endTime: Instant): TimeElapsed {
    val totalTimeBetween = Duration.between(startTime, endTime)
    val days = totalTimeBetween.toMillis() / 86400000L
    val millisOfCurrentDay = totalTimeBetween.toMillis() % 86400000L
    val hours: Long = millisOfCurrentDay / 3600000L
    val minutes: Long = (millisOfCurrentDay % 3600000L) / 60000L
    val seconds: Long = (millisOfCurrentDay % 60000L) / 1000L
    val milliseconds: Long = millisOfCurrentDay % 1000L

    return TimeElapsed(days, hours, minutes, seconds, milliseconds)
}

/**
 * This extension function prefixes zeros onto the resuting numeric string
 * to the desired length, by default it's 2, so if the current resulting
 * number falls below that length it will prefix a 0 onto it to increase
 * the length to that which's desired e.g. 2
 *
 * @param numStrLength the parameter defining the length at which you want
 * your number to stay in string form, prefixing it with zeros when it falls
 * below said length.
 */
private fun Long.zeroPrefixedString(numStrLength: Long = 2): String {
    var numOfZerosToPrefix = numStrLength - this.toString().length
    var resultingString = ""

    while (numOfZerosToPrefix > 0) {
        resultingString += "0"
        numOfZerosToPrefix--
    }

    resultingString += this

    return resultingString
}

private data class TimeElapsed(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val milliseconds: Long
)

sealed class LapState() {
    object Prepped: LapState();
    data class InProgress(val startTime: Instant): LapState();
    data class Completed(val startTime: Instant, val endTime: Instant): LapState()
}

@Preview
@Composable
private fun StopwatchPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Stopwatch("Testing", "Test reason", LapState.Prepped)
        }
    }
}