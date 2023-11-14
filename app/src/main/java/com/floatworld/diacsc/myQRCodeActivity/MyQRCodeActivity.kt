package com.floatworld.diacsc.myQRCodeActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.composes.QRCode.QRMainPage
import com.floatworld.diacsc.composes.QRCode.QRTopMenu
import com.floatworld.diacsc.myQRCodeActivity.ui.theme.DiacscTheme

class MyQRCodeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiacscTheme {
                MainCompose()
            }
        }
    }
}

@Composable
fun MainCompose(){
    Column(Modifier.fillMaxSize()) {
        QRTopMenu()
        QRMainPage()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    DiacscTheme {
        Box(modifier = Modifier.size(400.dp,500.dp)){
            MainCompose()
        }
    }
}