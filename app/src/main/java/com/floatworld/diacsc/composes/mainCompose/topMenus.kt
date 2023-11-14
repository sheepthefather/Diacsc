package com.floatworld.diacsc.composes.mainCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.viewModel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenus(){
    val model: MainViewModel = viewModel()
    val painter= painterResource(id = R.drawable.userimage)
    model.getUserImage(CurrentConfig.userID, painter)

    val isExpanded=remember { mutableStateOf(false) }

    val scope= rememberCoroutineScope()


    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(250, 250, 250))
        .padding(start = 10.dp, top = 10.dp, end = 5.dp, bottom = 10.dp)) {
        Image(modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable {
                scope.launch {
                    model.drawerState.open()
                }
            }
            ,painter = model.userImageMap[CurrentConfig.userID]!!, contentDescription = "菜单")
        Text(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(1f),
            text = CurrentConfig.userName,
            textAlign = TextAlign.Center)
        Box(modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp)) {
            Image(modifier = Modifier
                .clickable { isExpanded.value = !isExpanded.value }
                .size(30.dp),painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "添加",
                colorFilter = ColorFilter.tint(Color(0,0,0)))
            AddIconMenu(isExpanded = isExpanded)
        }
    }
}


//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(400.dp, 800.dp)){
//        TopMenus()
//    }
//}

