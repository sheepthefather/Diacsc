package com.floatworld.diacsc.composes.contacts

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.connectionActivity.ConnectionActivity
import com.floatworld.diacsc.viewModel.MainViewModel

@Composable
fun FriendRequireCard(userID:String){
    val model:MainViewModel= viewModel()
    model.getUserImage(userID, painterResource(id = R.drawable.userimage))
    model.getUserName(userID)
    Box(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier
            .heightIn(50.dp, 50.dp)
            .fillMaxSize()
            .padding(5.dp)) {
            Image(modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(40.dp)
                .clip(CircleShape),
                painter = model.userImageMap[userID]!!,
                contentDescription = "用户头像")
            Box(modifier=Modifier.align(Alignment.CenterVertically)) {
                Text(modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterStart),text = model.userName[userID]!!)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    IconButton(onClick = {
                        model.agreeAddFriendRequire(userID)
                    }) {
                        Icon(modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                            painter = painterResource(id = R.drawable.baseline_done_24),
                            contentDescription = "同意",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = {
                        model.deleteAddFriendRequire(userID)
                    }) {
                        Icon(modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.error, CircleShape),
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "拒绝",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview
//fun test(){
//    Box(modifier = Modifier.size(300.dp,200.dp)){
//        FriendRequireCard(userID = "1")
//    }
//}