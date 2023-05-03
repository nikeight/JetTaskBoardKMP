package com.jettaskboard.multiplatform.ui.components.board.bg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun StaticImagePlaceHolderCard(
    modifier: Modifier = Modifier,
    onImageClicked: (type: String) -> Unit,
    subTitle: String,
    drawableId: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .background(Color.Transparent)
                .clickable {
                    onImageClicked(subTitle)
                },
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = "background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = subTitle)
    }
}