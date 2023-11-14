package com.floatworld.diacsc.connectionActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.connectionActivity.ui.theme.DiacscTheme
import com.floatworld.diacsc.connectionActivity.viewModel.ConnectionViewModel
import com.floatworld.diacsc.composes.connection.BottomMenu
import com.floatworld.diacsc.composes.connection.ConnectionColumn
import com.floatworld.diacsc.composes.connection.TopMenu
import com.floatworld.diacsc.userInformation.CurrentConfig

class ConnectionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model=ViewModelProvider(this).get(ConnectionViewModel::class.java)
        setContent {
            DiacscTheme {
                ConnectionMain()
            }
        }
        model.personID = intent.getStringExtra("personID").toString()
        model.addToAppMessageList()
        model.getMessageList(model.personID, CurrentConfig.messagePageNum.toString())
    }
}

@Composable
fun ConnectionMain(){
    Column(modifier = Modifier.fillMaxSize()) {
        TopMenu()
        ConnectionColumn(
            Modifier
                .background(Color(240, 240, 240))
                .weight(1f)
                .fillMaxSize())
        BottomMenu()
    }
}