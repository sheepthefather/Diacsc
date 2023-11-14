package com.floatworld.diacsc.composes.QRCode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.floatworld.diacsc.myQRCodeActivity.MyQRCodeActivity
import com.floatworld.diacsc.userInformation.CurrentConfig

@Composable
fun QRTopMenu(){
    val context= LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(250, 250, 250))
        .padding(start = 5.dp, top = 3.dp, bottom = 3.dp, end = 5.dp)) {
        IconButton(onClick = {
            (context as MyQRCodeActivity).finish()
        }) {
            Icon(modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                ,painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "设置")
        }
        Text(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(1f),
            text = "我的二维码",
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.width(30.dp))
    }
}