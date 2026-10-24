package com.nxt.ui
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable fun FocusTile(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
 val interaction = remember { MutableInteractionSource() }
 val focused = interaction.collectIsFocusedAsState().value
 val scale = animateFloatAsState(if (focused) 1.08f else 1f, label = "focus").value
 Surface(onClick = onClick, modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }, interactionSource = interaction, shape = RoundedCornerShape(20.dp), color = if (focused) Color(0xFF314E77) else Color(0xB922314C), border = BorderStroke(if (focused) 3.dp else 1.dp, if (focused) Color(0xFF68D8FF) else Color(0x554F729A))) { androidx.compose.foundation.layout.Box(Modifier.padding(16.dp)) { content() } }
}
