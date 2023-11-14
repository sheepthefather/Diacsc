package com.floatworld.diacsc.composes.mainCompose

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.ActivityCollector
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.R
import com.floatworld.diacsc.loginActivity.LoginActivity
import com.floatworld.diacsc.myQRCodeActivity.MyQRCodeActivity
import com.floatworld.diacsc.settingActivity.SetingActivity
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.viewModel.MainViewModel
import com.floatworld.diacsc.userInformation.LocalInformationRepository
import com.floatworld.diacsc.userInformation.UserResourceRepository
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavgationDrawer(modifier:Modifier=Modifier,content:@Composable () -> Unit){
    val model: MainViewModel = viewModel()
    model.getUserImage(CurrentConfig.userID, painterResource(id = R.drawable.userimage))
    val context= LocalContext.current

    val backgroundFile= remember<Painter?> {
        val background=UserResourceRepository.getBackground()
        if(background!=null)
            return@remember background
        return@remember null
    }


    ModalNavigationDrawer(modifier = modifier.fillMaxSize(), drawerState = model.drawerState,drawerContent = {
        ModalDrawerSheet(modifier=Modifier.widthIn(min=200.dp,max=310.dp)) {
          Column{
                Box(modifier = Modifier.heightIn(200.dp)){
                    Image(painter = backgroundFile ?: painterResource(id = R.drawable.drawbackground), contentDescription = "背景")
                    Column(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)) {
                        Image(modifier = Modifier
                            .size(65.dp)
                            .clip(CircleShape),
                            painter = model.userImageMap[CurrentConfig.userID]!!,
                            contentDescription = "用户头像")
                        Text(text = CurrentConfig.userName,
                            fontWeight = FontWeight(700)
                        )
                        Text(text = "User ID: "+ CurrentConfig.userID,fontWeight = FontWeight(650))
                    }
                }
              //----------------------Item--------------------
              NavigationDrawerItem(icon = { Icon(painter = painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "设置")},
                  label = { Text(text = "设置", fontWeight = FontWeight.Bold) },
                  selected = false,
                  onClick = {
                      val intent=Intent(context,SetingActivity::class.java)
                      context.startActivity(intent)
                  })

              NavigationDrawerItem(icon = { Icon(painter = painterResource(id = R.drawable.baseline_qr_code_24),
                  contentDescription = "我的二维码")},
                  label = { Text(text = "我的二维码", fontWeight = FontWeight.Bold) },
                  selected = false,
                  onClick = {
                      val intent=Intent(context,MyQRCodeActivity::class.java)
                      context.startActivity(intent)
                  })

              NavigationDrawerItem(icon = { Icon(painter = painterResource(id = R.drawable.baseline_exit_to_app_24),
                  contentDescription = "退出登录")},
                  label = { Text(text = "退出登录", fontWeight = FontWeight.Bold) },
                  selected = false,
                  onClick = {
                      //exit login
                      CurrentConfig.apply {
                          userName=""
                          userID=""
                          passwd=""
                      }
                      LocalInformationRepository.saveUserInfo("","","")
                      ActivityCollector.finishAll()

                      val intent=Intent(context,LoginActivity::class.java)
                      context.startActivity(intent)
                      //stop ws Server
                      DiacscApplication.stopServer()
                  })
          }
            
        } }, content = content)
}

//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(200.dp,200.dp)){
//        NavgationDrawer() {
//            Column() {
//                Text(text = "1")
//                Text(text = "2")
//            }
//        }
//    }
//}