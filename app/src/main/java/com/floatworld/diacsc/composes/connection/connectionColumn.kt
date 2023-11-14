package com.floatworld.diacsc.composes.connection

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel
import kotlinx.coroutines.flow.filter
import com.floatworld.diacsc.userInformation.CurrentConfig

@Composable
fun ConnectionColumn(modifier: Modifier=Modifier){
    val model: ConnectionViewModel = viewModel()
    val listState= rememberLazyListState()
    //get message list
    LaunchedEffect(Unit){
        snapshotFlow{
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.filter { it==listState.layoutInfo.totalItemsCount - 1 }.collect{
            model.getMessageList(model.personID, CurrentConfig.messagePageNum.toString(10))
        }
    }

    LazyColumn(modifier = modifier, reverseLayout = true, state = listState){
        items(model.messageList){localMessage->
            UserConnectionCard(localmessage =localMessage)
        }
    }
}