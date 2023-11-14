package com.floatworld.diacsc

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.composes.contacts.ChatLogList
import com.floatworld.diacsc.composes.contacts.ContactsList
import com.floatworld.diacsc.composes.mainCompose.NavgationBar
import com.floatworld.diacsc.composes.mainCompose.NavgationDrawer
import com.floatworld.diacsc.composes.mainCompose.NavgationItem
import com.floatworld.diacsc.composes.mainCompose.TopMenus
import com.floatworld.diacsc.viewModel.MainViewModel
import com.floatworld.diacsc.ui.theme.DiacscTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiacscTheme {
                MainCompose()
            }
            val model=ViewModelProvider(this).get(MainViewModel::class.java)
            model.addToAppMessageList()
        }
    }
}

@Preview
@Composable
fun MainCompose(){
    val model = viewModel<MainViewModel>()
    val navSelectIndex=model.navSelectIndex
    NavgationDrawer {
        Column(modifier = Modifier
            .fillMaxSize()) {
            Surface(modifier = Modifier.padding(bottom = 2.dp),shadowElevation = 2.dp) {
                TopMenus()
            }
            if(navSelectIndex.value==0){
                ChatLogList(modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color(245, 245, 245))
                    .weight(1f))
            }else if(navSelectIndex.value==1){
                ContactsList(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(245, 245, 245))
                    .weight(1f))
            }
            Spacer(modifier = Modifier
                .border(1.dp, Color(200, 200, 200))
                .heightIn(1.dp, 1.dp)
                .fillMaxWidth())
            NavCompose()
        }
    }

}

@Composable
fun NavCompose(){
    val model = viewModel<MainViewModel>()
    val navSelectIndex= remember {
        model.navSelectIndex
    }

    NavgationBar(modifier = Modifier.background(Color(250,250,250))) {
        NavgationItem(modifier = Modifier.clickable { navSelectIndex.value=0 },
            selected = navSelectIndex.value==0,
            painter = painterResource(id = R.drawable.baseline_3p_24),
            contentDescription = "信息", text = "信息")


        NavgationItem(modifier = Modifier.clickable {
            navSelectIndex.value=1
            model.getUserList()},
            selected = navSelectIndex.value==1,
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "通讯录", text = "通讯录")
    }
}

@Preview
@Composable
fun test(){
    MainCompose()
}

