package com.floatworld.diacsc.composes.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.loginActivity.viewModel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInPage(){
    val model:LoginViewModel= viewModel()
    val context=LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        ) {

        OutlinedTextField(modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(
            painter = painterResource(id = R.drawable.baseline_person_outline_24),
            contentDescription = "用户名图标"
        )},value = model.id.value, onValueChange = {model.id.value=it}, label = {
            Text(text = "用户ID")
        }, isError = !model.isNum(model.id.value))

        OutlinedTextField(modifier = Modifier.fillMaxWidth(), leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.baseline_ket_24), contentDescription = "密码图标")
        },value = model.passwd.value, onValueChange = {model.passwd.value=it}, label = {
            Text(text = "密码")
        }, visualTransformation = {text->
            TransformedText(AnnotatedString("*".repeat(text.length)),OffsetMapping.Identity)
        }, isError = model.isError(model.passwd.value))
        Spacer(modifier = Modifier.height(50.dp))
        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(height = 50.dp, width = 200.dp),
            shape=MaterialTheme.shapes.small,onClick = { model.signIn(context) }) {

            if(!model.isLoading.value){
                Text(text = "登录")
            }else{
                CircularProgressIndicator(color=Color.White)
            }

        }
    }
}


//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(300.dp)){
//        LoginPage()
//    }
//}