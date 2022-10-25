package com.orion.mediaplayercompose.utils.extentions

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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