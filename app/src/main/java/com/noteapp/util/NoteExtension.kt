package com.noteapp.util

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.noteapp.util.NoteConstant.FLOAT_POINT_SIX
import com.noteapp.util.NoteConstant.FLOAT_POINT_TWO
import com.noteapp.util.NoteConstant.MINUS_TWO
import com.noteapp.util.NoteConstant.THOUSAND
import com.noteapp.util.NoteConstant.TWO

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(value = IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startX by transition.animateFloat(
        initialValue = MINUS_TWO * size.width.toFloat(),
        targetValue = TWO * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = THOUSAND)
        )
    )
    background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = FLOAT_POINT_SIX),
                Color.LightGray.copy(alpha = FLOAT_POINT_TWO),
                Color.LightGray.copy(alpha = FLOAT_POINT_SIX)
            ),
            startX = startX,
            endX = startX + size.width
        )
    ).onGloballyPositioned {
        size = it.size
    }
}