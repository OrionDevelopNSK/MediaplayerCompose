package com.orion.mediaplayercompose.utils.extentions

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun LazyListState.isHalfPastItemBottom(): Boolean {
    return firstVisibleItemScrollOffset > 500
}

fun CoroutineScope.scrollBasic(listState: LazyListState, left: Boolean = false) {
    launch {
        val pos = if (left) listState.firstVisibleItemIndex else listState.firstVisibleItemIndex + 1
        listState.animateScrollToItem(pos)
    }
}

fun LazyListState.isHalfPastItemTop(): Boolean {
    return firstVisibleItemScrollOffset > 500
}

fun Long.toMinutesAndSeconds(duration : Long) : String{
    val min: Int = (duration / (60 * 1000)).toInt()
    val sec = ((duration - min * 60 * 1000) / 1000f).roundToInt()
    return String.format("%02d", min) + ":" + String.format("%02d", sec)
}