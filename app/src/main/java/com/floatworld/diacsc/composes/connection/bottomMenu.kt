package com.floatworld.diacsc.composes.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomMenu(){
    val model:ConnectionViewModel= viewModel()
    val inputText= remember {
        mutableStateOf("")
    }
    Row(
        Modifier
            .background(Color.White)
            .heightIn(30.dp, 120.dp)
            .fillMaxWidth()) {
        CustomEdit(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(1f),
                value = inputText.value, onChange = {inputText.value=it})
        Button(modifier=Modifier.align(Alignment.CenterVertically),onClick = {
            model.sendMessage(inputText.value)
            inputText.value=""
        }) {
            Text(text = "发送")
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}

//@Preview
//@Composable
//fun Test(){
//    Box(modifier = Modifier
//        .background(Color(95, 95, 95, 255))
//        .size(width = 300.dp, 450.dp)){
//        BottomMenu()
//    }
//}