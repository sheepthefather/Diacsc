package com.floatworld.diacsc.userInfoActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.composes.userInfo.UserInfoMainPage
import com.floatworld.diacsc.composes.userInfo.UserInforTop
import com.floatworld.diacsc.userInfoActivity.ui.theme.DiacscTheme
import com.floatworld.diacsc.userInfoActivity.viewModel.UserInfoViewModel

class UserInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model=ViewModelProvider(this).get(UserInfoViewModel::class.java)
        val id=intent.getStringExtra("id")
        if (id != null) {
            model.setUserID(id)
        }
        setContent {
            DiacscTheme {
                MainCompose()
            }
        }
    }
}

@Composable
fun MainCompose(){
    Column {
        Surface(modifier = Modifier.height(60.dp), shadowElevation = 2.dp) {
            UserInforTop()
        }
        UserInfoMainPage()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    DiacscTheme {
        MainCompose()
    }
}