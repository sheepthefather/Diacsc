package com.floatworld.diacsc.addFriendsActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.R
import com.floatworld.diacsc.addFriendsActivity.ui.theme.DiacscTheme
import com.floatworld.diacsc.addFriendsActivity.viewModel.AddFriendViewModel
import com.floatworld.diacsc.composes.addFirend.FindFriendError
import com.floatworld.diacsc.composes.addFirend.SearchText
import com.floatworld.diacsc.userInformation.CurrentConfig

class AddFriendActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiacscTheme {
                AddNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNavigation(){
    val model:AddFriendViewModel= viewModel()
    val context= LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
       Surface(modifier = Modifier
           .fillMaxWidth()
           .height(60.dp)
           .shadow(1.dp)) {
           Box(modifier = Modifier.fillMaxSize()) {
               IconButton(modifier = Modifier.align(Alignment.CenterStart),onClick = {
                   (context as AddFriendActivity).finish()
               }) {
                   Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "返回")
               }
               Text(modifier = Modifier.align(Alignment.Center),text = "添加好友")
           }
       }
        SearchText(modifier = Modifier.fillMaxWidth())
        Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = "我的ID:"+CurrentConfig.userID)
    }

    if(model.isError.value){
        FindFriendError()
    }
}
