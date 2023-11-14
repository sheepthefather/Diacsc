package com.floatworld.diacsc.composes.login

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.loginActivity.viewModel.LoginViewModel

@Composable
fun ErrorDialog(){
    val model:LoginViewModel= viewModel()
    AlertDialog(onDismissRequest = { model.isError.value=false }, confirmButton = {
        TextButton(onClick = { model.isError.value=false }) {
            Text(text = "确认")
        }
    }, title = {
        Text(text = "错误") }, text = {
            Text(text = model.errorMessage.value)
    })
}