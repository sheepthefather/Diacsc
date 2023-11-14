package com.floatworld.diacsc.loginActivity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.composes.login.ErrorDialog
import com.floatworld.diacsc.composes.login.SignInPage
import com.floatworld.diacsc.composes.login.SignUpPage
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel
import com.floatworld.diacsc.loginActivity.ui.theme.DiacscTheme
import com.floatworld.diacsc.loginActivity.viewModel.LoginViewModel

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiacscTheme {
                LoginCompose()
            }
        }
        val model= ViewModelProvider(this).get(LoginViewModel::class.java)
        model.signIn(this,true)

    }
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
@Composable
fun LoginCompose(){
    val model:LoginViewModel= viewModel()
    val isSignInOrSignUp=remember{
        mutableStateOf(true)
    }
    Column {
        Spacer(modifier = Modifier.height(100.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(modifier = Modifier.pointerInput(Unit){
                while (true){
                    awaitPointerEventScope { awaitFirstDown().position }
                    isSignInOrSignUp.value=true
                }
            },text = "登录", fontSize = 25.sp, color = if(isSignInOrSignUp.value)Color.Unspecified else Color.Gray)
            Spacer(modifier = Modifier.width(50.dp))
            Text(modifier = Modifier.pointerInput(Unit){
                while (true){
                    awaitPointerEventScope { awaitFirstDown().position }
                    isSignInOrSignUp.value=false
                }
            },text = "注册", fontSize = 25.sp, color = if(!isSignInOrSignUp.value)Color.Unspecified else Color.Gray)
        }
        AnimatedVisibility(visible = isSignInOrSignUp.value) {
            SignInPage()
        }
        AnimatedVisibility(visible = !isSignInOrSignUp.value) {
            SignUpPage()
        }
    }

    if(model.isError.value){
        ErrorDialog()
    }

}

@Preview
@Composable
fun test(){
    Box(modifier = Modifier.size(600.dp,600.dp)){
        DiacscTheme {
            LoginCompose()
        }
    }
}
