package com.floatworld.diacsc.settingActivity.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import com.floatworld.diacsc.ActivityCollector
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.MainActivity
import com.floatworld.diacsc.logic.sha.Sha256
import com.floatworld.diacsc.loginActivity.LoginActivity
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.LocalInformationRepository
import com.floatworld.diacsc.userInformation.UserInformationDepository
import com.floatworld.diacsc.userInformation.UserResourceRepository
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel:ViewModel() {
    val dialogID= mutableStateOf(DialogEnum.NONE)
    var changeImageID= ChangeImageEnum.HEADPORTRAIT

    fun showChangeNameDialog(){
        dialogID.value=DialogEnum.NAME
    }

    fun changeUserName(newName:String){
        if(checkUserNameError(newName)||newName.isEmpty())
            return
        UserInformationDepository.changeUserName(newName){
            dialogID.value=DialogEnum.NONE
            CurrentConfig.userName=newName

            LocalInformationRepository.saveUserInfo(CurrentConfig.userID,CurrentConfig.userName,CurrentConfig.passwd)

            val intent=Intent("com.floatworld.diacsc.FORCE_OFFLINE")
            intent.setPackage(DiacscApplication.context.packageName)
            DiacscApplication.context.sendBroadcast(intent)
        }
    }

    fun changeUserPasswd(newPasswd:String){
        if(checkUserPasswdError(newPasswd)||newPasswd.isEmpty())
            return
        val sha256=Sha256.getSha256(newPasswd)
        UserInformationDepository.changeUserPasswd(sha256){
            dialogID.value=DialogEnum.NONE

            CurrentConfig.passwd=sha256
            LocalInformationRepository.saveUserInfo(CurrentConfig.userID,CurrentConfig.userName,CurrentConfig.passwd)

            val intent=Intent("com.floatworld.diacsc.FORCE_OFFLINE")
            intent.setPackage(DiacscApplication.context.packageName)
            DiacscApplication.context.sendBroadcast(intent)
        }
    }

    fun changeUserImage(uri:Uri){
        when(changeImageID){
            ChangeImageEnum.HEADPORTRAIT->{
                UserInformationDepository.changeUserHeadportrait(uri){
                    CoroutineScope(Dispatchers.IO).launch {
                        UserResourceRepository.clearUserImage(CurrentConfig.userID)
                        UserResourceRepository.getUserImage(CurrentConfig.userID){}
                        withContext(Dispatchers.Main){
                            val intent=Intent("com.floatworld.diacsc.FORCE_OFFLINE")
                            intent.setPackage(DiacscApplication.context.packageName)
                            DiacscApplication.context.sendBroadcast(intent)
                        }
                    }
                }
            }
            ChangeImageEnum.BACKGROUND->{
                CoroutineScope(Dispatchers.IO).launch {
                    UserResourceRepository.saveBackground(uri.toFile().readBytes())
                    withContext(Dispatchers.Main){
                        val intent=Intent("com.floatworld.diacsc.FORCE_OFFLINE")
                        intent.setPackage(DiacscApplication.context.packageName)
                        DiacscApplication.context.sendBroadcast(intent)
                    }
                }
            }
            else->{}
        }
    }

    fun showChangePasswdDialog(){
        dialogID.value=DialogEnum.PASSWD
    }


    fun checkUserNameError(newName: String): Boolean {
        val regex=Regex("""^[^\s/\\=&?'"\r\n]*$""")
        return !regex.containsMatchIn(newName)
    }

    fun checkUserPasswdError(newPasswd: String):Boolean{
        val regex=Regex("""^[0-9]*$""")
        return !regex.containsMatchIn(newPasswd)
    }
}