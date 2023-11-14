package com.floatworld.diacsc.composes.addFirend

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.addFriendsActivity.viewModel.AddFriendViewModel

@Composable
fun FindFriendError(){
    val model:AddFriendViewModel= viewModel()
    AlertDialog(onDismissRequest = { model.isError.value=false }, confirmButton = {
        TextButton(onClick = { model.isError.value=false }) {
            Text(text = "确认")
        }
    }, title = {
        Text(text = "错误") }, text = {
        Text(text = "请求的用户ID错误")
    })
}