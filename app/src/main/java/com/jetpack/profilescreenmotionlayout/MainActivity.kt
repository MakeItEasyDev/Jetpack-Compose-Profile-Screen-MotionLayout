package com.jetpack.profilescreenmotionlayout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutDebugFlags
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import com.jetpack.profilescreenmotionlayout.ui.theme.ProfileScreenMotionLayoutTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreenMotionLayoutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Profile Page Animation",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        ProfileScreenMotionLayout()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreenMotionLayout() {
    var componentHeight by remember { mutableStateOf(1000f) }
    var corners by remember { mutableStateOf(400F) }
    val swipeableState = rememberSwipeableState(initialValue = "Bottom")
    val anchors = mapOf(0f to "Bottom", componentHeight to "Top")
    val mprogress = (swipeableState.offset.value / componentHeight)

    MotionLayout(
        motionScene = MotionScene(
            """
                {
                  ConstraintSets: {
                      start: {
                        circle: {
                          width: 72,
                          height: 72,
                          end: ['parent', 'end',24],
                          top: ['content','top'],
                          bottom: ['content','top'],
                          translationZ: 1
                        },
                        box1: {
                          width: 150,
                          height: 220,
                          start: ['parent', 'start',36],
                          top: ['parent','top',190],
                          translationZ: 1
                        },
                          title: {
                          width: 'spread',
                          height: 'wrap',
                          start: ['box1', 'end', 16],
                          end: ['parent', 'end', 16],
                          top: ['content','top', 44],
                          translationZ: 1
                        },
                        content: {
                          width: 'spread',
                          height: 'spread',
                          start: ['parent', 'start'],
                          end: ['parent', 'end'],
                          top: ['parent','top', 260],
                          bottom: ['parent','bottom'],
                        }
                      },
                      end: {circle: {
                          width: 0,
                          height: 0,
                          end: ['parent', 'end',82],
                          top: ['content','top'],
                          bottom: ['content','top'],
                          translationZ: 1
                        },
                       box1: {
                          width: 150,
                          height: 220,
                          start: ['parent', 'start',36],
                          top: ['parent','top',90],
                          translationZ: 0
                        },
                        title: {
                          width: 'spread',
                          height: 'wrap',
                          start: ['parent', 'start',16],
                          end: ['parent', 'end', 16],
                          top: ['parent','top', 16],
                          translationZ: 1
                        },content: {
                          width: 'spread',
                          height: 'spread',
                          start: ['parent', 'start'],
                          end: ['parent', 'end'],
                          top: ['box','bottom'],
                          bottom: ['parent','bottom'],
                          translationZ: 1
                        }
                      }
                    },
                    Transitions: {
                      default: {
                        from: 'start',
                        to: 'end',
                        pathMotionArc: 'startHorizontal',
                        KeyFrames: {
                          KeyPositions: [
                            {
                           type: 'parentRelative',
                               target: ['box1'],
                               frames: [40],
                               percentX: [0.0],
                               percentY: [-0.18]
                            }
                          ],
                          KeyAttributes: [
                            {
                              target: ['box1'],
                              frames: [50],
                              rotationZ: [60],
                              //rotationY: [25], 
                            }
                          ]
                        }
                      }
                    }
                }
            """.trimIndent()
        ),
        progress = mprogress,
        debug = EnumSet.of(MotionLayoutDebugFlags.NONE),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                reverseDirection = true,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical
            )
            .onSizeChanged { size ->
                componentHeight = size.height.toFloat()
                corners = 400 - ((mprogress * 100) * 4)
            }
    ) {
        Box(
            modifier = Modifier
                .layoutId("content")
                .clip(CutCornerShape(topStart = corners))
                .background(Color.LightGray)
        )
        Box(
            modifier = Modifier
                .layoutId("box1")
                .background(Color.Green)
        )
        Text(
            text = "Make it Easy",
            fontSize = 18.sp,
            modifier = Modifier.layoutId("title")
        )
        Box(
            modifier = Modifier
                .layoutId("circle")
                .clip(CircleShape)
                .background(Color.Red)
        )
    }
}


















