package com.floatworld.diacsc.composes.contacts

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.connectionActivity.ConnectionActivity
import com.floatworld.diacsc.viewModel.MainViewModel

@Composable
fun UserCard(id:String,name:String){
    val model: MainViewModel =viewModel()
    model.getUserImage(id, painterResource(id = R.drawable.userimage))

    val context=LocalContext.current
    Row(modifier = Modifier.clickable {//start ConnectionActivity
            val intent=Intent(context,ConnectionActivity::class.java)
            intent.apply {
                putExtra("personID",id)
            }
            context.startActivity(intent)
        }.heightIn(50.dp, 50.dp)
        .fillMaxSize()
        .padding(5.dp)) {
        Image(modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(40.dp)
            .clip(CircleShape),
            painter = model.userImageMap[id]!!,
            contentDescription = "用户头像")
        Box(modifier = Modifier.fillMaxSize()) {
            Text(modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterStart),text = name)
        }

    }
}


//@Preview
//@Composable
//fun Test(){
//    DiacscTheme {
//        Box(modifier = Modifier.size(width = 200.dp, height = 60.dp)) {
//            UserCard()
//        }
//    }
//}