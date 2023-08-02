package com.khalidtouch.streamer.core.designsystem.extensions

data class ScrollingInfo(
    val isScrollingDown: Boolean = false,
    val isFar: Boolean = false,
) {

    fun and(condition: Boolean) = if (condition) this else copy(
        isScrollingDown = !isScrollingDown, isFar = !isFar,
    )
}