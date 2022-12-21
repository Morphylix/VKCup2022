package com.morphylix.android.dzentest.utils

import android.view.View
import android.view.animation.DecelerateInterpolator
import com.yuyakaido.android.cardstackview.*

fun View.dp() = resources.displayMetrics.density

fun CardStackView.swipe(dir: Direction, cardStackLayoutManager: CardStackLayoutManager) {
    val swipeSetting = SwipeAnimationSetting.Builder()
        .setDuration(Duration.Slow.duration)
        .setInterpolator(DecelerateInterpolator())

    if (dir == Direction.Right) {
        val swipeRightSetting = swipeSetting
            .setDirection(Direction.Right)
            .build()
        cardStackLayoutManager.setSwipeAnimationSetting(swipeRightSetting)
        this.swipe()
    }
    if (dir == Direction.Left) {
        val swipeLeftSetting = swipeSetting
            .setDirection(Direction.Left)
            .build()
        cardStackLayoutManager.setSwipeAnimationSetting(swipeLeftSetting)
        this.swipe()
    }
}