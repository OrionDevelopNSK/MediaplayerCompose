package com.orion.mediaplayercompose.utils

import androidx.compose.foundation.lazy.LazyListState
import com.orion.mediaplayercompose.utils.extentions.isHalfPastItemBottom
import com.orion.mediaplayercompose.utils.extentions.isHalfPastItemTop
import com.orion.mediaplayercompose.utils.extentions.scrollBasic
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

fun snappyLazyColumn(listState: LazyListState, coroutineScope: CoroutineScope){
    if (!listState.isScrollInProgress) {
        if (listState.isHalfPastItemTop()) {
            coroutineScope.scrollBasic(listState, left = true)
        } else {
            coroutineScope.scrollBasic(listState)
        }

        if (listState.isHalfPastItemBottom()) {
            coroutineScope.scrollBasic(listState)
        } else {
            coroutineScope.scrollBasic(listState, left = true)
        }
    }
}

fun toMinutesAndSeconds(duration : Long) : String{
    val min: Int = (duration / (60 * 1000)).toInt()
    val sec = ((duration - min * 60 * 1000) / 1000f).roundToInt()
    return String.format("%02d", min) + ":" + String.format("%02d", sec)
}