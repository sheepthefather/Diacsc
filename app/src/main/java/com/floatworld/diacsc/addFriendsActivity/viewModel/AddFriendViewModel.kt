package com.floatworld.diacsc.addFriendsActivity.viewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.floatworld.diacsc.userInfoActivity.UserInfoActivity
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.UserInformationDepository

class AddFriendViewModel:ViewModel() {
    val id= mutableStateOf("")
    val isError= mutableStateOf(false)

    fun getUser(id:String,context:Context){
        if(id==CurrentConfig.userID){
            isError.value=true
            return
        }
        UserInformationDepository.getUserName(id){
            if(it.isEmpty()){
                isError.value=true
                return@getUserName
            }
            val intent=Intent(context,UserInfoActivity::class.java)
            intent.apply {
                putExtra("id",id)
            }
            context.startActivity(intent)
        }
    }
}