package com.floatworld.diacsc.composes.contacts

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.userInformation.UserNameID
import com.floatworld.diacsc.viewModel.MainViewModel

@Composable
fun ContactsList(modifier: Modifier=Modifier){
    val model: MainViewModel = viewModel()
    LaunchedEffect(Unit){
        model.getUserList()
        model.getAddFriendRequire()
    }

    LazyColumn(modifier = modifier){
        items(model.addFriendRequire){friendRequire->
            FriendRequireCard(userID = friendRequire.requestUserID)
        }
        items(model.userList){userNameID ->
            UserCard(id = userNameID.id, name = userNameID.name)
        }
    }
}
//@Preview
//@Composable
//fun Test(){
//    ContactsList()
//}