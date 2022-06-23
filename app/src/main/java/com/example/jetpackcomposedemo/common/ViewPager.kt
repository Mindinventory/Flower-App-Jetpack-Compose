package com.example.jetpackcomposedemo.common

//import androidx.compose.animation.animatedFloat
//import androidx.compose.animation.core.OnAnimationEnd
//import androidx.compose.foundation.animation.FlingConfig
//import androidx.compose.foundation.animation.fling
//import androidx.compose.ui.draw.drawOpacity
//import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
//import androidx.compose.ui.layout.WithConstraints
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

interface ViewPagerScope {
    val index: Int
    fun next()
    fun previous()
}

private data class ViewPagerImpl(override val index: Int, val increment: (Int) -> Unit) :
    ViewPagerScope {
    override fun next() {
        increment(1)
    }

    override fun previous() {
        increment(-1)
    }
}

data class PageState(
    val alpha: Float = 1f,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val translationX: Float = 0f,
    val translationY: Float = 0f
)

private const val MIN_SCALE = 0.75f

private const val MIN_SCALE_ZOOM = 0.9f
private const val MIN_ALPHA = 0.7f

interface ViewPagerTransition {
    fun transformPage(constraints: Constraints, position: Float): PageState

    companion object {
        val NONE = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return PageState()
            }
        }

        val DEPTH_TRANSFORM = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return when {
                    position <= 0 -> PageState()
                    position <= 1 -> {
                        val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                        PageState(
                            1 - position, scaleFactor, scaleFactor,
                            constraints.maxWidth * -position
                        )
                    }
                    else -> PageState(0f, 0f, 0f)
                }
            }
        }

        val ZOOM_OUT = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return when {
                    position <= 1 && position >= -1 -> {
                        val scaleFactor = MIN_SCALE_ZOOM.coerceAtLeast(1 - abs(position))
                        val vertMargin = constraints.maxHeight * (1 - scaleFactor) / 2
                        val horzMargin = constraints.maxWidth * (1 - scaleFactor) / 2
                        val translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        val alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM)) * (1 - MIN_ALPHA)))
                        PageState(
                            alpha,
                            scaleFactor,
                            scaleFactor,
                            translationX
                        )
                    }
                    else -> PageState(0f, 0f, 0f)
                }
            }
        }
    }
}

@Composable
fun ViewPager(
    modifier: Modifier = Modifier,
    onPageChange: (Int) -> Unit = {},
    range: IntRange? = null,
    startPage: Int = 0,
    enabled: Boolean = true,
//    useAlpha: Boolean = false,
//    transition: ViewPagerTransition = ViewPagerTransition.NONE,
    screenItem: @Composable() ViewPagerScope.() -> Unit
) {
    if (range != null && !range.contains(startPage)) {
        throw IllegalArgumentException("The start page supplied was not in the given range")
    }

    val coroutineScope = rememberCoroutineScope()
    val splineDec: DecayAnimationSpec<Float> = rememberSplineBasedDecay()

    Box {
        BoxWithConstraints {
            val alphas = remember { mutableListOf(1f, 1f, 1f) }
            val index = remember { mutableStateOf(startPage) }

            var width = constraints.maxWidth.toFloat()
//            val offset = animatedFloat(width)
//            offset.setBounds(0f, 2 * width)
            /*var offset by remember {  //Current position        //try 1
                mutableStateOf(Offset.Zero)
            }
            offset = Offset(0f, 2 * width)*/
            var offset = remember {    //try 2 - Animatable
                Animatable(0f)
            }
//            offset.setBounds(0f, 2 * width)

            val anchors = remember { listOf(0f, width, 2 * width) }

//            val flingConfig = FlingConfig(
//                anchors,
//                animationSpec = SpringSpec(dampingRatio = 0.8f, stiffness = 1000f),
//            )
            /*val flingConfig = FlingConfig(
                anchors,
                animationSpec = SpringSpec(dampingRatio = 0.8f, stiffness = 1000f),
            )
            val onAnimationEnd: OnAnimationEnd = { reason, end, _ ->
                if (reason != AnimationEndReason.Interrupted) {
                    if (end == width * 2) {
                        index.value += 1
                    } else if (end == 0f) {
                        index.value -= 1
                    }
                }
            }*/

            fun indexCheck() {
                if (range != null) {
                    when (index.value) {
                        range.first -> Offset(width, 2 * width)
                        range.last -> Offset(0f, width)
                        else -> Offset(0f, 2 * width)
                    }
                }
            }

//            onDispose {
//                // snapTo -> Sets the current value to the target value immediately, without any animation.
//                width.snapTo(width)
//            }

            DisposableEffect(index.value) {
                onDispose {
                    coroutineScope.launch {
                        offset.snapTo(width)
//                          width = (width)
                    }
                }
            }

//            onCommit(index.value) {
            LaunchedEffect((index.value)) {
                indexCheck()
                /* if (range != null) {
                     if (index.value < range.first) {
                         index.value = range.first
                     } else if (index.value > range.last) {
                         index.value = range.last
                     }
                 }*/
                onPageChange(index.value)
            }

            val increment: (Int) -> Unit = { increment: Int ->
                coroutineScope.launch {
                    offset.animateTo(
                        width * sign(increment.toDouble()).toFloat() + width,
                        /*onEnd = { animationEndReason, _ ->        //????????????????????????????????????????
                            if (animationEndReason != AnimationEndReason.Interrupted) {
                                index.value += increment
                            offset.snapTo(width)
//                                offset = (width)
                            }
                        }*/
                    )
                }
            }

//            val draggable = modifier.draggable(
//                orientation = Orientation.Horizontal,
//                onDrag = {
//                    val old = offset.value
//                    offset.snapTo(offset.value - (it * 0.7f))
//                    offset.value - old
//                }, onDragStopped = { offset.fling(-(it * 0.7f), flingConfig, onAnimationEnd) },
//                enabled = enabled
//            )


            val draggable = modifier.draggable(
                orientation = Orientation.Horizontal,
                state = DraggableState { draggedFloat ->
                    width = draggedFloat
                },
                onDragStarted = {
                    val old = offset.value
                    offset.snapTo(old - (width * 0.7f))
                    offset.value - old
                },
                onDragStopped = {
                    offset.animateDecay(
                        -(it * 0.7f)
//                    , flingConfig, onAnimationEnd
                        , animationSpec = splineDec
                    )
                },
                enabled = enabled
            )

            Box(draggable) {
                Layout(
                    content = {
                        for (x in -1..1) {
//                            Box(Modifier.layoutId(x + 1).drawOpacity(alphas[x + 1])) {
                            Box(
                                Modifier
                                    .layoutId(x + 1)
                                    .alpha(alphas[x + 1])
                            ) {
                                screenItem(
                                    ViewPagerImpl(index.value + x, increment)
                                )
                            }
                        }
                    },
                    modifier = modifier
                ) { measurables, constraints ->
                    val placeables = measurables.map { it.measure(constraints) to it.layoutId }
                    val height = placeables.maxByOrNull { it.first.height }?.first?.height ?: 0

                    layout(constraints.maxWidth, height) {
                        placeables.forEach { (placeable, tag) ->
                            if (tag is Int) {
//                                placeable.place(
//                                    x = (constraints.maxWidth * tag)
//                                            - offset.value.toInt(),
//                                    y = 0
//                                )
                                placeable.place(
                                    x = (constraints.maxWidth * tag)
                                            - offset.value.toInt(),
                                    y = 0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
