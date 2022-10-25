package com.orion.mediaplayercompose.utils

import androidx.compose.foundation.lazy.LazyListState
import com.orion.mediaplayercompose.utils.extentions.isHalfPastItemBottom
import com.orion.mediaplayercompose.utils.extentions.isHalfPastItemTop
import com.orion.mediaplayercompose.utils.extentions.scrollBasic
import kotlinx.coroutines.CoroutineScope

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