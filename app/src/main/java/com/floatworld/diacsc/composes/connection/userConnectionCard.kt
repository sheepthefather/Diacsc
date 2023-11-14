package com.floatworld.diacsc.composes.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel
import com.floatworld.diacsc.logic.chatMessage.LocalMesssage
import kotlinx.coroutines.flow.filter
import com.floatworld.diacsc.userInformation.CurrentConfig

@Composable
fun UserConnectionCard(modifier: Modifier=Modifier,localmessage:LocalMesssage){
    val model:ConnectionViewModel= viewModel()
    model.getUserMeAndOther(painterResource(id = R.drawable.userimage))
    LaunchedEffect(Unit){
        snapshotFlow {
            model.messageList[0]
        }.filter { (!it.isSend && model.isAnimate.value) ||
                (model.isAnimate.value&&model.messageList[0].senderID==model.personID) }.collect{
            model.isAnimate.value=false
            model.connectionPosition.snapTo(50.dp)
            model.connectionPosition.animateTo(0.dp)
        }
    }
    val position= remember {
        mutableStateOf(0.dp)
    }
    if(model.messageList[0].ID==localmessage.ID){
        position.value=model.connectionPosition.value
    }

    Box(modifier= modifier.offset {
        IntOffset(position.value.toPx().toInt(),0)
    }){
        if(localmessage.senderID== CurrentConfig.userID){
            Row(modifier = Modifier.padding(start = 50.dp, end = 5.dp, bottom = 10.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(modifier = Modifier.align(Alignment.End),text = CurrentConfig.userName, fontSize = 12.sp)
                    Surface(modifier = Modifier.align(Alignment.End),shape = MaterialTheme.shapes.medium) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            Row() {
                                Text(text = localmessage.message)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Image(modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                        painter = model.userImage["me"]!!, contentDescription = "我方头像")
                    if(!localmessage.isSend)
                        Icon(modifier = Modifier
                            .size(16.dp)
                            ,painter = painterResource(id = R.drawable.baseline_schedule_send_24), contentDescription = "发送成功")
                }
            }
        }else{
            Row(modifier = Modifier.padding(start = 5.dp, end = 50.dp, bottom = 10.dp)) {
                Image(modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                    painter = model.userImage["other"]!!, contentDescription = "对方头像")
                Spacer(modifier = Modifier.width(5.dp))
                Column() {
                    Text(text = model.personName.value, fontSize = 12.sp)
                    Surface(shape = MaterialTheme.shapes.medium) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            Text(text = localmessage.message)
                        }
                    }
                }

            }
        }
    }

}


//@Preview
//@Composable
//fun test(){
//    Box(modifier=Modifier.size(200.dp)){
//        UserConnectionCard("123",true,Date())
//    }
//}