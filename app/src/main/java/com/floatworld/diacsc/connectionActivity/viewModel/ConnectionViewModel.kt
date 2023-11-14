package com.floatworld.diacsc.connectionActivity.viewModel

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.chatMessage.LocalMesssage
import com.floatworld.diacsc.logic.messageList.AppMessage
import com.floatworld.diacsc.logic.messageList.AppMessageList
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.UserInformationDepository
import com.floatworld.diacsc.userInformation.UserResourceRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ConnectionViewModel:ViewModel() {
    val messageList= mutableStateListOf<LocalMesssage>()
    val personName= mutableStateOf("")
    val userImage= mutableStateMapOf<String,Painter>()
    var connectionPosition=Animatable(0.dp, Dp.VectorConverter)
    var isAnimate= mutableStateOf(false)
    var personID="0"

    var page=0
    var isEnd=false

    fun getUserName(id:String){
        UserInformationDepository.getUserName(id){ name->
            personName.value=name
        }
    }

    fun getUserMeAndOther(defaultPainter: Painter){
        if(userImage["me"]!=null && userImage["other"]!=null)
            return
        userImage["me"]=defaultPainter
        userImage["other"]=defaultPainter
        UserResourceRepository.getUserImage(CurrentConfig.userID){ painter ->
            userImage["me"]=painter
        }
        UserResourceRepository.getUserImage(personID){ painter ->
            userImage["other"]=painter
        }
    }

    fun getMessageList(id:String,pageSize:String){
        if(isEnd)
            return
        UserInformationDepository.getUserMessageList(id,page.toString(10),pageSize){
            if(it.size<pageSize.toInt())
                isEnd=true
            this.page=page+1
            if(messageList.isEmpty()){
                val localMesssageList= mutableListOf<LocalMesssage>()
                it.forEach {
                    localMesssageList.add(LocalMesssage(it,true))
                }
                messageList.addAll(localMesssageList)
                return@getUserMessageList
            }
            for(i in 0 until it.size){
                if(messageList.last().ID>it[i].ID){
                    val localMesssageList= mutableListOf<LocalMesssage>()
                    it.subList(i,it.size).forEach {
                        localMesssageList.add(LocalMesssage(it,true))
                    }
                    messageList.addAll(localMesssageList)
                    return@getUserMessageList
                }
            }

        }
    }

    fun sendMessage(message:String){
        if(DiacscApplication.binder==null)
            return
        if(message.isEmpty())
            return
        val id=System.currentTimeMillis().toString()
        DiacscApplication.binder.sendMessage(id,personID,message){_,newID->
            for (i in 0 until messageList.size){
                if(messageList[i].ID==id){
                    messageList[i]=LocalMesssage(newID,messageList[i].senderID,messageList[i].receiverID,messageList[i].message,messageList[i].date,true)
                }
            }
        }
        //UTC Time
        val dateFormatUtc =SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
        val utcTime = dateFormatUtc.format(Date());
        localSendMessage(AppMessage(id, CurrentConfig.userID,personID,message,utcTime),false)
    }

    fun addToAppMessageList(){
        AppMessageList.list.add(::addMesssage)
    }

    override fun onCleared() {
        super.onCleared()
        AppMessageList.list.remove(::addMesssage)
    }

    private fun addMesssage(appMessage: AppMessage,isSend:Boolean){
        messageList.add(0, LocalMesssage(appMessage,isSend))
        isAnimate.value=true
    }

    private  fun localSendMessage(appMessage: AppMessage,isSend: Boolean=false){
        AppMessageList.list.forEach {callBack->
            callBack(appMessage,isSend)
        }
    }
}