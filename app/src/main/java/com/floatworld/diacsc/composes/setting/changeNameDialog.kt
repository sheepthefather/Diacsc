package com.floatworld.diacsc.composes.setting

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.settingActivity.viewModel.DialogEnum
import com.floatworld.diacsc.settingActivity.viewModel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNameDialog(){
    val model:SettingViewModel= viewModel()
    val newName= rememberSaveable{
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = { model.dialogID.value = DialogEnum.NONE },
        confirmButton = {
            TextButton(onClick = {
                model.changeUserName(newName.value)
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
            Text(text = "修改名称")
        }, text = {
            OutlinedTextField(
                value = newName.value,
                onValueChange = { newName.value = it },
                label = {
                    Text(text = "姓名")
                },
                singleLine = true,
                isError = model.checkUserNameError(newName.value)
            )
        }
    )
}