package com.ryanmolyneux.letsgooocomposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun DottedHeadingsHorizontalScroller(
    headings: List<String>,
    headingsColorBox: Color,
    onTapHeadingsBox: ((headingText: String, headingIndex: Int) -> Unit)? = null,
    headingsTextColor: Color? = null,
    headingsTextSize: TextUnit? = null,
    spaceBetweenHeadingsBoxs: Dp? = null,
    dotsColor: Color? = null,
    dotsSize: Dp? = null,
    numOfDots: Int? = null
) {
    val DEFAULT_SPACE_BETWEEN_ITEMS = 4.dp
    val DEFAULT_TEXT_SIZE = 16.sp
    val DEFAULT_DOTS_SIZE = 2.dp
    val DEFAULT_NUM_DOTS = 60

    Box {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            content = {
                itemsIndexed(headings) { index, _ ->
                    Surface(
                        color = headingsColorBox,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = spaceBetweenHeadingsBoxs ?: DEFAULT_SPACE_BETWEEN_ITEMS)
                            .clickable {
                                onTapHeadingsBox?.invoke(headings[index], index)
                            }
                    ) {
                        Text(
                            headings[index],
                            fontSize = headingsTextSize ?: DEFAULT_TEXT_SIZE,
                            color = headingsTextColor ?: Color.Unspecified,
                            modifier = Modifier
                                .padding(2.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically)
                                .wrapContentWidth()
                        )
                    }
                }
            })
        Row(
            modifier = Modifier
                .matchParentSize()
                .zIndex(0f),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 1..(numOfDots ?: DEFAULT_NUM_DOTS)) {
                Surface(
                    color = dotsColor ?: headingsColorBox,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(dotsSize ?: DEFAULT_DOTS_SIZE)
                ) {}
            }
        }
    }
}

@Preview
@Composable
fun DottedHeadingsHorizontalScrollerManyItems() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var headingsText = mutableListOf<String>(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6",
            "Item 7",
            "Item 8"
        )
        DottedHeadingsHorizontalScroller(
            headingsText,
            headingsColorBox = Color(204, 102, 153),
            headingsTextColor = Color.White,
            headingsTextSize = 16.sp,
            dotsColor = Color(179, 255, 218),
            onTapHeadingsBox = { headingText, headingIndex ->
                headingsText.reverse()
            }
        )
    }
}

@Preview
@Composable
fun DottedHeadingsHorizontalScrollerOneItem() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        DottedHeadingsHorizontalScroller(
            listOf(
                "Item 1",
                "Item 20000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "Item 324",
                "Item 1234"
            ),
            headingsColorBox = Color(204, 102, 153),
            headingsTextColor = Color.White,
            headingsTextSize = 16.sp
        )
    }
}

@Preview
@Composable
fun DottedHeadingsHorizontalScrollerTwoItemsWithLongText() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DottedHeadingsHorizontalScroller(
            listOf("Item Oneeeee", "Item Twooooooo"),
            headingsColorBox = Color(204, 102, 153),
            headingsTextColor = Color.White,
            headingsTextSize = 16.sp,
            dotsColor = Color(179, 255, 218)
        )
    }
}