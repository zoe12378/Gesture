package com.example.gesture

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.example.gesture.ui.theme.GestureTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestureTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //Greeting("Android")
                    //PointerEvents ()
                    Tap()
                    Drag_Horizontal()
                    Drag_Vertical()
                    Drag()
                }
            }
        }
    }
}
@Composable
fun Drag() {
    var offset1 by remember { mutableStateOf(Offset.Zero) }
    var offset2 by remember { mutableStateOf(Offset(500f,500f)) }
    var height by remember { mutableStateOf(0) }
    var width by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()){

        Box(modifier = Modifier
            .onGloballyPositioned { coordinates ->
                height = coordinates.size.height
                width = coordinates.size.width
            }
            .offset { IntOffset(offset1.x.roundToInt(), offset1.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    offset1 += dragAmount
                }
            }
        ){
            Image(
                painter = painterResource(id = R.drawable.ghost1),
                contentDescription = "精靈1",
            )
        }
    }
    Box(modifier = Modifier
        .offset { IntOffset(offset2.x.roundToInt(), offset2.y.roundToInt()) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                offset2 += dragAmount
            }
        }
    ){
        Image(
            painter = painterResource(id = R.drawable.ghost2),
            contentDescription = "精靈2",
        )
    }

}

@Composable
fun Drag_Vertical() {
    var offsetY by remember { mutableStateOf(0f) }
    Text(
        text = "垂直拖曳",
        modifier = Modifier
            .offset { IntOffset(500, offsetY.toInt()) }
            .draggable(
                orientation= Orientation.Vertical,
                state = rememberDraggableState{ delta ->
                    offsetY += delta
                }
            )
    )
}

@Composable
fun Drag_Horizontal() {
    var offsetX by remember { mutableStateOf(0f) }
    Text(
        text = "水平拖曳",
        modifier = Modifier
            .offset() { IntOffset(offsetX.toInt(), 80) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                }
            )
    )
}

@Composable
fun PointerEvents() {
    var msg by remember { mutableStateOf("") }
    Column {
        Text(msg)
        Box(
            Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Yellow)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            msg = "${event.type}, ${event.changes.first().position}"
                        }
                    }
                }
        )
    }
}
@Composable
fun Tap() {
    var msg by remember { mutableStateOf("TAP相關手勢實例") }
    var offset1 by remember { mutableStateOf(Offset.Zero) }
    var offset2 by remember { mutableStateOf(Offset.Zero) }
    var PU = arrayListOf(R.drawable.pu0, R.drawable.pu1,
        R.drawable.pu2, R.drawable.pu3,
        R.drawable.pu4, R.drawable.pu5)
    var Number by remember { mutableStateOf(0) }

    Column {
        Text(text = msg)

        Image(
            painter = painterResource(id = PU[Number]),
            contentDescription = "靜宜之美",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { change, dragAmount -> offset2 += dragAmount },
                        onDragStart = {
                            offset1 = it
                            offset2 = it
                        },
                        onDragEnd = {
                            if (offset2.x >= offset1.x) {
                                msg = "長按後向右拖曳"
                                Number++
                                if (Number > 5) {
                                    Number = 0
                                }
                            } else {
                                msg = "長按後向左拖曳"
                                Number--
                                if (Number < 0) {
                                    Number = 5
                                }
                            }
                        }
                    )

                }


        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GestureTheme {
        Greeting("Android")
    }
}