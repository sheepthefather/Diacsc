package com.floatworld.diacsc.settingActivity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.R
import com.floatworld.diacsc.composes.setting.ChangeNameDialog
import com.floatworld.diacsc.composes.setting.ChangePasswdDialog
import com.floatworld.diacsc.composes.setting.SettingTopMenu
import com.floatworld.diacsc.settingActivity.ui.theme.DiacscTheme
import com.floatworld.diacsc.settingActivity.viewModel.ChangeImageEnum
import com.floatworld.diacsc.settingActivity.viewModel.DialogEnum
import com.floatworld.diacsc.settingActivity.viewModel.SettingViewModel
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.yalantis.ucrop.UCrop
import java.io.File

class SetingActivity : BaseActivity() {
    lateinit var model:SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model=ViewModelProvider(this).get(SettingViewModel::class.java)
        setContent {
            DiacscTheme {
                MainCompose()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK&&requestCode==UCrop.REQUEST_CROP){
            if(data==null)
                return
            UCrop.getOutput(data)?.let {
                model.changeUserImage(it)
            }
        }else if(resultCode==UCrop.RESULT_ERROR){
            Toast.makeText(applicationContext,"change Image error",Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCompose() {
    val context= LocalContext.current
    val model:SettingViewModel= viewModel()
    val primaryColor=MaterialTheme.colorScheme.primary.toArgb()
    val singleImagePickerHeadportraitLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri->
            if (uri==null)
                return@rememberLauncherForActivityResult
            val dir=context.getExternalFilesDir("tempHeadportrait")
            val file=File(dir,"image.png")
            val options=UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.PNG)
            options.withAspectRatio(1f,1f)
            options.setActiveControlsWidgetColor(primaryColor)
            UCrop.of(
                uri,
                Uri.fromFile(file)).
                withOptions(options).
                start(context as Activity)
        })

    val singleImagePickerBackgroundLauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri->
            if (uri==null)
                return@rememberLauncherForActivityResult
            val dir=context.getExternalFilesDir("tempBackground")
            val file=File(dir,"image.png")
            val options=UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.PNG)
            options.setActiveControlsWidgetColor(primaryColor)
            UCrop.of(
                uri,
                Uri.fromFile(file)).
            withOptions(options).
            start(context as Activity)
        })
    Column(modifier = Modifier.fillMaxSize()) {
        SettingTopMenu()
        Column(modifier = Modifier.fillMaxSize()) {
            ListItem(modifier =
            Modifier.clickable {
                model.changeImageID=ChangeImageEnum.HEADPORTRAIT
                singleImagePickerHeadportraitLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
                headlineText = { Text("修改头像") },
                supportingText = {
                    Text("点击修改头像")
                },
                leadingContent = {
                    Icon(
                        painterResource(id = R.drawable.baseline_person_outline_24),
                        contentDescription = "点击修改头像图标",
                    )
                },
                trailingContent = { Icon(modifier = Modifier.rotate(270f),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "修改") }
            )


            ListItem(modifier =
                Modifier.clickable {
                    model.showChangeNameDialog()
                },
                headlineText = { Text("修改姓名") },
                supportingText = {
                    Text("当前姓名:"+CurrentConfig.userName)
                },
                leadingContent = {
                    Icon(
                        painterResource(id = R.drawable.baseline_badge_24),
                        contentDescription = "点击修改姓名图标",
                    )
                },
                trailingContent = { Icon(modifier = Modifier.rotate(270f),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "修改") }
            )

            ListItem(modifier =
            Modifier.clickable {
                model.showChangePasswdDialog()
            },
                headlineText = { Text("修改密码") },
                supportingText = {
                    Text("点击修改密码")
                },
                leadingContent = {
                    Icon(
                        painterResource(id = R.drawable.baseline_pin_24),
                        contentDescription = "点击修改密码图标",
                    )
                },
                trailingContent = { Icon(modifier = Modifier.rotate(270f),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "修改") }
            )


            ListItem(modifier =
            Modifier.clickable {
                model.changeImageID=ChangeImageEnum.BACKGROUND
                singleImagePickerBackgroundLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
                headlineText = { Text("修改背景") },
                supportingText = {
                    Text("点击修改背景")
                },
                leadingContent = {
                    Icon(
                        painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "点击修改背景图标",
                    )
                },
                trailingContent = { Icon(modifier = Modifier.rotate(270f),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "修改") }
            )
        }
    }

    when(model.dialogID.value){
        DialogEnum.NONE->{}
        DialogEnum.NAME->{
            ChangeNameDialog()
        }
        DialogEnum.PASSWD->{
            ChangePasswdDialog()
        }
        else->{}
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiacscTheme {
        MainCompose()
    }
}