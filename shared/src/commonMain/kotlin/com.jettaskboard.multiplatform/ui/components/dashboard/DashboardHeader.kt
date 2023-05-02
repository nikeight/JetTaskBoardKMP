package com.jettaskboard.multiplatform.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    modifier: Modifier,
    title: String,
    showIcon: Boolean = false,
    icon: ImageVector = Icons.Filled.MoreVert,
    onMenuItemClicked: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .background(color = Color(0xFF2c2c2e))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
        ) {
            Text(
                modifier = modifier
                    .padding(16.dp)
                    .weight(1f),
                text = title,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400
            )
            if (showIcon) {
                IconButton(
                    onClick = { onMenuItemClicked() },
                    modifier = modifier
                        .align(CenterVertically)
                ) {
                    Icon(
                        imageVector = icon,
                        tint = Color.White,
                        contentDescription = "Menu Icon"
                    )
                }
            }
        }
    }
}
