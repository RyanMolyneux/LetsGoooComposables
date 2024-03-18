package com.ryanmolyneux.letsgooocomposables.maths

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.abs
import kotlin.math.sign


/**
 * Composable visualisation for a box and whiskers plot,
 * where upper bounds is always expected to be greater than
 * lower bounds.
 * additional notes
 * - Step size defaults to 1, although it can be moved down or
 *   moved up although you should note making it to low
 *   can ruin the visibility of the text due to the limted space
 *   on screen.
 */
@Composable
fun BoxAndWhiskersPlot(q1: Float,
                       q2: Float,
                       q3: Float,
                       upperBounds: Float,
                       lowerBounds: Float,
                       stepSize: Float = 1f,
                       lineColour: Color = Color.Black,
                       boxColor: Color = Color.Blue) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        val canvasHeight = this.size.height
        val canvasWidth = this.size.width
        val canvasCenter = canvasHeight/2f
        val lineStart = canvasWidth/10f
        val lineEnd = canvasWidth - lineStart
        val lineRange = lineEnd - lineStart
        val whiskerYStart = canvasCenter - canvasCenter/10f
        val whiskerYEnd = canvasCenter + canvasCenter/10f
        val range: Float = abs(upperBounds - lowerBounds)
        val upperAndLowerPositive = sign(upperBounds) >= 0 && sign(lowerBounds) >= 0
        val textHeight = textMeasurer.measure("100").size.height.toFloat()

        fun drawRangeLine() {
            val hLineYCoordinate = whiskerYEnd + canvasCenter/10f
            val vLineStart = hLineYCoordinate - (canvasCenter/100f)
            val vLineEnd = hLineYCoordinate + (canvasCenter/100f)
            val stepWiseRange = range/stepSize

            drawLine(lineColour, start = Offset(lineStart, hLineYCoordinate), end = Offset(lineEnd, hLineYCoordinate))
            for(i in 0..stepWiseRange.toInt()) {
                val vLineXCoordinate = lineStart + (lineRange * i/stepWiseRange)
                val text = (lowerBounds + (range * (i/stepWiseRange))).toString()
                val textWidth = textMeasurer.measure(text).size.width
                drawLine(lineColour, start = Offset(vLineXCoordinate, vLineStart), end = Offset(vLineXCoordinate, vLineEnd))
                drawText(textMeasurer = textMeasurer,
                         text = AnnotatedString(text, SpanStyle(Color.Black)),
                         topLeft = Offset(vLineXCoordinate - textWidth/2f, vLineEnd),
                         size = Size(lineRange/stepWiseRange, textHeight))
            }
        }
        fun drawLineAndWhiskers() {
            drawLine(lineColour, start = Offset(lineStart, canvasCenter), end = Offset(lineEnd, canvasCenter))
            drawLine(lineColour, start = Offset(lineStart, whiskerYStart), end = Offset(lineStart, whiskerYEnd))
            drawLine(lineColour, start = Offset(lineEnd, whiskerYStart), end = Offset(lineEnd, whiskerYEnd))
        }
        fun drawBox() {
            fun calculateQXCoordinate(qValue: Float): Float {
                return if (upperAndLowerPositive) {
                    lineStart + (lineRange * abs(qValue / range))
                } else {
                    lineEnd - (lineRange * abs(qValue / range))
                }
            }
            val q1XCoordinate = calculateQXCoordinate(q1)
            val q2XCoordinate = calculateQXCoordinate(q2)
            val q3XCoordinate = calculateQXCoordinate(q3)
            drawRect(boxColor, topLeft = Offset(q1XCoordinate, whiskerYStart), size = Size((q3XCoordinate-q1XCoordinate), (whiskerYEnd-whiskerYStart)))
            drawLine(color = lineColour, start = Offset(q2XCoordinate, whiskerYStart), end = Offset(q2XCoordinate, whiskerYEnd))
        }
        drawRangeLine()
        drawLineAndWhiskers()
        drawBox()
    }
}

@Preview
@Composable
fun BoxAndWhiskersPreview() {
    BoxAndWhiskersPlot(q1 = 1f, q2 = 2f, q3 = 3f, upperBounds = 5f, lowerBounds = 0f, boxColor = Color.Cyan)
}

@Preview
@Composable
fun BoxAndWhiskersMinusPreview() {
    BoxAndWhiskersPlot(q1 = -1f, q2 = -2f, q3 = -3f, upperBounds = 0f, lowerBounds = -5f)
}
