/*
 * Copyright (C) 2022 Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.gpstest.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.gpstest.ui.dashboard.Arrow

/**
 * A collapsible card that expands and collapses when tapped. [topContent] is always shown,
 * and when the card is expanded the [expandedContent] is shown. It has a chevron icon in the lower
 * right corner that flips when expanded/collapsed. [onClick] is called when the card is tapped with
 * the new state of the card (true if it's now expanded, false if it's now collapsed).
 * [initialExpandedState] can be used to set the initial expanded state of the card (e.g., from
 * a user preference or an expanded state from the last app execution).
 */
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun CollapsibleCard(
    topContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    initialExpandedState: Boolean = false,
    onClick: (newExpandedState: Boolean) -> Unit,
) {
    var expandedState by rememberSaveable { mutableStateOf(initialExpandedState) }
    val transitionState = remember {
        MutableTransitionState(expandedState).apply {
            targetState = !expandedState
        }
    }
    val transition = updateTransition(transitionState, label = "rotate-expand")
    val arrowRotationDegree by transition.animateFloat({
        tween()
    }, label = "rotate-expand") {
        if (expandedState) 180f else 0f
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(5.dp)
            .clickable {
                expandedState = !expandedState
                onClick(expandedState)
            },
        elevation = 2.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                topContent()
                if (expandedState) {
                    expandedContent()
                }
            }
            Arrow(
                rotation = arrowRotationDegree,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
