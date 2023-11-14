package com.floatworld.diacsc.composes.userInfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.floatworld.diacsc.R
import com.floatworld.diacsc.userInfoActivity.UserInfoActivity

@Composable
fun UserInforTop(){
    val context= LocalContext.current
    Box(modifier = Modifier.fillMaxWidth()){
        IconButton(modifier = Modifier.align(Alignment.CenterStart),onClick = {
            (context as UserInfoActivity).finish()
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "返回图标")
        }
        Text(modifier = Modifier.align(Alignment.Center),text = "添加好友")
    }
}