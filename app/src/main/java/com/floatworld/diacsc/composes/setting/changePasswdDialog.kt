package com.floatworld.diacsc.composes.setting

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.settingActivity.viewModel.DialogEnum
import com.floatworld.diacsc.settingActivity.viewModel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswdDialog(){
    val model: SettingViewModel = viewModel()
    val newPasswd= rememberSaveable{
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = { model.dialogID.value = DialogEnum.NONE },
        confirmButton = {
            TextButton(onClick = {
                model.changeUserPasswd(newPasswd.value)
            }) {
                Text(text = "修改")
            }
        },
        dismissButton = {
            TextButton(onClick = { model.dialogID.value= DialogEnum.NONE }) {
                Text(text = "取消")
            }
        },
        title = {
            Text(text = "修改密码")
        }, text = {
            OutlinedTextField(
                value = newPasswd.value,
                onValueChange = { newPasswd.value = it },
                label = {
                    Text(text = "密码")
                },
                singleLine = true,
                isError = model.checkUserPasswdError(newPasswd.value),
                visualTransformation = {text->
                    TransformedText(AnnotatedString("*".repeat(text.length)), OffsetMapping.Identity)}
            )
        }
    )
}