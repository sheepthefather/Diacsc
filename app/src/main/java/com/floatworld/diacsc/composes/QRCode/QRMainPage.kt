package com.floatworld.diacsc.composes.QRCode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.myQRCodeActivity.viewModel.MyQRCodeViewModel
import com.floatworld.diacsc.userInformation.CurrentConfig

@Composable
fun QRMainPage(){
    val model:MyQRCodeViewModel= viewModel()
    val painter=painterResource(id = R.drawable.userimage)
    LaunchedEffect(Unit){
        model.getUserImage()
        model.getQRCode()
    }
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 100.dp)) {
            Row() {
                Image(modifier = Modifier.size(75.dp),
                    painter = model.userImagePainter.value?:painter,
                    contentDescription = "用户头像")
                Spacer(modifier = Modifier.width(15.dp))
                Text(modifier = Modifier.align(Alignment.CenterVertically),
                    text = CurrentConfig.userName, fontSize = 30.sp,
                    maxLines = 1)
            }
            Spacer(modifier = Modifier.height(30.dp))
            Image(modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally),
                painter = model.userQRImagePainter.value?:painter, contentDescription = "扫描二维码")
        }
    }
}

@Composable
@Preview
fun test(){
    Box(modifier = Modifier.size(400.dp,500.dp)){
        QRMainPage()
    }
}