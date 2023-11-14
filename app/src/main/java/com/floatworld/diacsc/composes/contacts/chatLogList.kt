package com.floatworld.diacsc.composes.contacts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.logic.jsonParser.ChatLog
import com.floatworld.diacsc.viewModel.MainViewModel

@Composable
fun ChatLogList(modifier:Modifier=Modifier){
    val model:MainViewModel= viewModel()
    model.getChatLogList()
    LazyColumn(modifier = modifier){
        items(model.chatLogList){ chatLog ->
            UserMessageCard(chatLog.ID,chatLog.senderID,chatLog.receiverID,chatLog.message,chatLog.time)
        }
    }
}

//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(300.dp)){
//        ChatLogList()
//    }
//}


