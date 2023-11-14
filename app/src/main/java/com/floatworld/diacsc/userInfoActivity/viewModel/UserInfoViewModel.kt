package com.floatworld.diacsc.userInfoActivity.viewModel

import android.content.Context
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.floatworld.diacsc.BaseActivity
import com.floatworld.diacsc.userInfoActivity.UserInfoActivity
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.UserInformationDepository
import com.floatworld.diacsc.userInformation.UserResourceRepository

class UserInfoViewModel:ViewModel() {
    val image= mutableStateOf<Painter?>(null)
    val name= mutableStateOf("")
    val userID= mutableStateOf("1")
    val isFriend= mutableStateOf(false)
    fun getUserInformation(defaultPainter: Painter){
        if(image.value!=null)
            return
        image.value=defaultPainter
        UserResourceRepository.getUserImage(userID.value){
            image.value=it
        }
        UserInformationDepository.getUserName(userID.value){
            name.value=it
        }
        UserInformationDepository.getIsFriend(userID.value){
            isFriend.value=it
        }
    }

    fun setUserID(id:String){
        userID.value=id
    }

    fun addFriend(id:String,context:Context){
        UserInformationDepository.addFriendRequest(id){
            (context as UserInfoActivity).finish()
        }
    }

    fun deleteFriend(id: String,context: Context){

    }
}