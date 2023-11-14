package com.floatworld.diacsc.loginActivity.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.MainActivity
import com.floatworld.diacsc.logic.sha.Sha256
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.InternetOperationDepository
import com.floatworld.diacsc.userInformation.LocalInformationRepository
import com.floatworld.diacsc.userInformation.UserInformationDepository

class LoginViewModel:ViewModel() {
    val id= mutableStateOf("")
    val name= mutableStateOf("")
    val passwd= mutableStateOf("")
    val repeatPasswd= mutableStateOf("")
    val errorMessage= mutableStateOf("")
    val isError= mutableStateOf(false)
    val isLoading= mutableStateOf(false)

    fun signIn(context: Context,isAutoSignIn:Boolean=false){
        if(isAutoSignIn){
            val userData= LocalInformationRepository.getUserInfo()
            if(userData.id==""||userData.name==""||userData.passwd==""){
                return
            }

            UserInformationDepository.login(userData.id,userData.passwd){ isSuccess, userID, userName->
                if(!isSuccess){
                    isError.value=true
                    isLoading.value=false
                    errorMessage.value="账号或者密码错误"
                    return@login
                }

                if(userID==null || userName==null){
                    errorMessage.value="服务器错误，无法验证ID"
                    isLoading.value=false
                    return@login
                }
                startMainActivity(context,userID,userName,userData.passwd)
            }
            return
        }


        if(id.value.isEmpty()||passwd.value.isEmpty()){
            isError.value=true
            errorMessage.value="ID或密码不能为空"
            return
        }else if(!isNum(id.value) || isError(passwd.value)){
            errorMessage.value="id或密码的格式不正确"
            isError.value=true
            return
        }

        val sha256=Sha256.getSha256(passwd.value)

        isLoading.value=true
        UserInformationDepository.login(id.value,sha256){ isSuccess, userID, userName->
            if(!isSuccess){
                isError.value=true
                isLoading.value=false
                errorMessage.value="账号或者密码错误"
                return@login
            }

            if(userID==null || userName==null){
                errorMessage.value="服务器错误，无法验证ID"
                isLoading.value=false
                return@login
            }
            //ID name passwd save to file
            LocalInformationRepository.saveUserInfo(userID,userName,sha256)

            startMainActivity(context,userID,userName,sha256)
        }
    }

    fun signUp(context: Context){
        if(isError(name.value)||isError(passwd.value)||isError(repeatPasswd.value)){
            errorMessage.value="用户名或密码不合法"
            isError.value=true
            return
        }
        if((passwd.value!=repeatPasswd.value)){
            errorMessage.value="两次输入密码不正确"
            isError.value=true
            return
        }
        //get passwd sha256 to register user
        val sha256=Sha256.getSha256(passwd.value)
        isLoading.value=true
        InternetOperationDepository.register(name.value,sha256){ status, userId, userName ->
            if(!status){
                isLoading.value=false
                errorMessage.value="注册失败"
                isError.value=true
                return@register
            }
            if(userId==null||userName==null){
                isLoading.value=false
                errorMessage.value="服务器返回数据失败"
                isError.value=true
                return@register
            }

            id.value=userId
            //login
            signIn(context)

        }
    }

    fun startMainActivity(context: Context,userID:String,userName:String,sha256:String){
        CurrentConfig.userID=userID
        CurrentConfig.passwd=sha256
        CurrentConfig.userName=userName
        DiacscApplication.createServer()
        val intent=Intent(context,MainActivity::class.java)
        context.startActivity(intent)

        //finish me
        (context as Activity).finish()
    }


    fun isError(str: String):Boolean{
        val regex=Regex("""^[^\s/\\=&?'"\r\n]*$""")
        return !regex.containsMatchIn(str)

    }

    fun isNum(str: String):Boolean{
        val regex=Regex("""^[0-9]*$""")
        return regex.containsMatchIn(str)
    }
}