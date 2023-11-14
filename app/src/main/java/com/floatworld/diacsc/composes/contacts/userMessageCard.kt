package com.floatworld.diacsc.composes.contacts

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.connectionActivity.ConnectionActivity
import com.floatworld.diacsc.viewModel.MainViewModel
import com.floatworld.diacsc.userInformation.CurrentConfig
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun UserMessageCard(ID:String,senderID:String,receiverID:String,content:String,time:String){
    val model:MainViewModel=viewModel()
    val userID=if(senderID== CurrentConfig.userID)receiverID else senderID
    model.getUserImage(userID, painterResource(id = R.drawable.userimage))
    model.getUserName(userID)

    val context=LocalContext.current
    Row(modifier = Modifier.clickable {
        val intent=Intent(context,ConnectionActivity::class.java)
        intent.apply {
            putExtra("personID",userID)
        }
        context.startActivity(intent)

    }.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) {
        Image(modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(40.dp)
            .clip(CircleShape),
            painter = model.userImageMap[userID]!!,
            contentDescription = "用户头像")
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                Box(modifier = Modifier.fillMaxWidth()){
                    Text(text = model.userName[userID]!!)
                    val messageTime=ZonedDateTime.ofInstant(Instant.parse(time),ZoneId.systemDefault()).toLocalDateTime().toString()
                    Text(modifier = Modifier.align(Alignment.TopEnd),text = messageTime)
                }
                Row {
                    Text(color = Color.Gray,text = content, maxLines = 1)
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(300.dp)){
//        DiacscTheme {
//            UserMessageCard()
//        }
//    }
//}
