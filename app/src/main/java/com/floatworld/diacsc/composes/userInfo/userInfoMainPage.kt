package com.floatworld.diacsc.composes.userInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.userInfoActivity.viewModel.UserInfoViewModel

@Composable
fun UserInfoMainPage(){
    val model:UserInfoViewModel= viewModel()
    val context= LocalContext.current
    model.getUserInformation(painterResource(R.drawable.userimage))
    Column() {
        Row(modifier = Modifier.padding(10.dp)) {
            Image(modifier = Modifier.size(64.dp).clip(CircleShape),
                painter = model.image.value!!,
                contentDescription = "用户头像",)
            Column(modifier=Modifier.padding(start = 10.dp)) {
                Text(text = model.name.value)
                Text(text = "UserID:"+model.userID.value)
            }
        }
        Button(modifier = Modifier.fillMaxWidth(),colors = ButtonDefaults.buttonColors(containerColor = Color.White,
            contentColor = Color.Black),
            onClick = {
                if(!model.isFriend.value){
                    model.addFriend(model.userID.value,context)
                }else{

                }
            }) {
            Text(text = if(model.isFriend.value)"删除好友" else "添加好友")
        }

    }
}


@Preview
@Composable
fun test(){
    Box(modifier = Modifier.size(300.dp)) {
        UserInfoMainPage()
    }
}