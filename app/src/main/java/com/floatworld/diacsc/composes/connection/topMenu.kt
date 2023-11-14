package com.floatworld.diacsc.composes.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.connectionActivity.ConnectionActivity
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel

@Composable
fun TopMenu(){
    val context=LocalContext.current
    val model:ConnectionViewModel= viewModel()
    LaunchedEffect(Unit){
        model.getUserName(model.personID)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(250, 250, 250))
        .padding(start = 5.dp, top = 3.dp, bottom = 3.dp, end = 5.dp)) {
        IconButton(onClick = {
            (context as ConnectionActivity).finish()
        }) {
            Icon(modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                ,painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "菜单")
        }
        Text(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(1f),
            text = model.personName.value,
            textAlign = TextAlign.Center)
        Box(modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp)) {
            Icon(modifier = Modifier
                .size(30.dp),painter = painterResource(id = R.drawable.baseline_segment_24),
                contentDescription = "添加")
        }
    }
}