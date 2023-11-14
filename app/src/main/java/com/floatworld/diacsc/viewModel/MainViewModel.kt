package com.floatworld.diacsc.viewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.beust.klaxon.Klaxon
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.jsonParser.ChatLog
import com.floatworld.diacsc.logic.jsonParser.FriendRequire
import com.floatworld.diacsc.logic.jsonParser.QRAddFriendJSON
import com.floatworld.diacsc.logic.messageList.AppMessage
import com.floatworld.diacsc.logic.messageList.AppMessageList
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.UserResourceRepository
import com.floatworld.diacsc.userInformation.UserInformationDepository
import com.floatworld.diacsc.userInformation.UserNameID

class MainViewModel:ViewModel() {
    val userList= mutableStateListOf<UserNameID>()
    val chatLogList= mutableStateListOf<ChatLog>()
    var userImageMap= mutableStateMapOf<String,Painter>()
    val addFriendRequire= mutableStateListOf<FriendRequire>()
    var userName= mutableStateMapOf<String,String>()

    @OptIn(ExperimentalMaterial3Api::class)
    var drawerState=DrawerState(DrawerValue.Closed)

    val navSelectIndex = mutableStateOf(0)

    fun getUserList(){
        UserInformationDepository.getUserList{ list->
            userList.clear()
            userList.addAll(list)
        }
    }

    fun getUserImage(id:String,defaultPainter: Painter){
        if(!userImageMap.containsKey(id)){
            userImageMap[id]=defaultPainter
            UserResourceRepository.getUserImage(id){ painter->
                //in main thread
                userImageMap[id]=painter
            }
        }
    }

    fun getChatLogList(){
        UserInformationDepository.getChatLogList { chatLogs ->
            chatLogList.clear()
            chatLogList.addAll(chatLogs)
        }
    }

    fun getAddFriendRequire(){
        UserInformationDepository.getAddFriendRequire {
            addFriendRequire.clear()
            addFriendRequire.addAll(it)
        }
    }

    fun agreeAddFriendRequire(friendID: String){
        UserInformationDepository.agreeAddFriendRequire(friendID){
            addFriendRequire.removeIf {
                return@removeIf it.requestUserID==friendID
            }

            //update user list
            getUserList()
            getChatLogList()
        }
    }

    fun deleteAddFriendRequire(friendID:String){
        UserInformationDepository.deleteAddFriendRequire(friendID){
            addFriendRequire.removeIf {
                return@removeIf it.requestUserID==friendID
            }
        }
    }

    fun QRAddFriend(qrStr: String?){
        try {
            if (qrStr==null)
                throw Exception()
            val qrAddFriendJSON=Klaxon().parse<QRAddFriendJSON>(qrStr)
            if(qrAddFriendJSON==null)
                throw Exception()
            UserInformationDepository.qrAddFriend(qrAddFriendJSON){
                getUserList()
                getChatLogList()
            }
        }catch (e:Exception){
            Toast.makeText(DiacscApplication.context,"扫描错误",Toast.LENGTH_SHORT).show()
        }

    }

    fun getUserName(id:String){
        if(userName.get(id)==null){
            userName[id]=""
        }
        UserInformationDepository.getUserName(id){
            userName[id]=it
        }
    }

    fun addToAppMessageList(){
        AppMessageList.list.add(::addMesssage)
    }

    override fun onCleared() {
        super.onCleared()
        AppMessageList.list.remove(::addMesssage)
    }

    private fun addMesssage(appMessage: AppMessage,isSend: Boolean){
        var isChange=false
        for (i in 0 until chatLogList.size){
            val inUserID=if(appMessage.senderID== CurrentConfig.userID) appMessage.receiverID else appMessage.senderID
            val existChatLog=if(chatLogList[i].senderID== CurrentConfig.userID) chatLogList[i].receiverID else chatLogList[i].senderID
            if(inUserID==existChatLog){
                chatLogList[i]=ChatLog(appMessage.ID,appMessage.senderID,appMessage.receiverID,appMessage.message,appMessage.date)
                if(i!=0){   //if message isn't first then swap value of chatLogList
                    val temp=chatLogList[0]
                    chatLogList[0]=chatLogList[i]
                    chatLogList[i]=temp
                }
                isChange=true
                break
            }
        }
        if(!isChange){
            chatLogList.add(0,ChatLog(appMessage.ID,appMessage.senderID,appMessage.receiverID,appMessage.message,appMessage.date))
        }
    }
}