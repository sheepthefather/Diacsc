package com.floatworld.diacsc.composes.mainCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun NavgationItem(modifier: Modifier=Modifier,
        selected:Boolean=false,
        frontColor:Color=Color.Black,
        backColor:Color=Color.Gray,
        painter: Painter,contentDescription:String,text:String){
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Image(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(24.dp),
            painter = painter, contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(if(selected)frontColor else backColor))
        Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = text,
            fontSize = MaterialTheme.typography.bodySmall.fontSize)
    }
}