package com.floatworld.diacsc.userInformation

import android.net.Uri
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.internet.HttpCallbackListener
import com.floatworld.diacsc.logic.internet.UserInformation
import com.floatworld.diacsc.logic.jsonParser.AddFriendConstruct
import com.floatworld.diacsc.logic.jsonParser.AgreeAddFriendRequireConstrut
import com.floatworld.diacsc.logic.jsonParser.ChangeUserHeadportraitConstruct
import com.floatworld.diacsc.logic.jsonParser.ChangeUserNameConstruct
import com.floatworld.diacsc.logic.jsonParser.ChangeUserPasswdContruct
import com.floatworld.diacsc.logic.jsonParser.ChatLog
import com.floatworld.diacsc.logic.jsonParser.DeleteFriendRequireConstruct
import com.floatworld.diacsc.logic.jsonParser.FriendRequire
import com.floatworld.diacsc.logic.jsonParser.IsFriend
import com.floatworld.diacsc.logic.jsonParser.Login
import com.floatworld.diacsc.logic.jsonParser.QRAddFriendJSON
import com.floatworld.diacsc.logic.jsonParser.QRAddfriendConstruct
import com.floatworld.diacsc.logic.messageList.AppMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

object UserInformationDepository {
    private val userListLocal= mutableListOf<UserNameID>()
    fun getUserList(callback:(MutableList<UserNameID>)->Unit){
        UserInformation.getUserList(
            CurrentConfig.userID,
            CurrentConfig.passwd,object:HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                val gson=Gson()
                val typeOf=object :TypeToken<MutableList<UserNameID>>() {}.type

                //callback in main
                coroutineScope {
                    withContext(Dispatchers.Main){
                        userListLocal.clear()
                        userListLocal.addAll(gson.fromJson<MutableList<UserNameID>>(String(response),typeOf))
                        callback(userListLocal)
                    }
                }
            }
            override fun onError() {
                println("error")
            }
        })
    }

    fun getIsFriend(id:String,callback:(Boolean)->Unit){
        UserInformation.getIsFriend(id,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isFriend=Klaxon().parse<IsFriend>(String(response))
                    if(isFriend==null){
                        throw java.lang.Exception("isFriend is null")
                    }
                    withContext(Dispatchers.Main){
                        callback(isFriend.isFriend)
                    }
                }catch (e:Exception){
                    Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun addFriendRequest(id:String,callback: () -> Unit){
        UserInformation.addFriendRequest(id,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<AddFriendConstruct>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw java.lang.Exception("addFriendError")
                    callback()
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,"addFriendError",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun getAddFriendRequire(callback: (List<FriendRequire>) -> Unit){
        UserInformation.getAddFriendRequire(object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val arrayFriendRequire=Klaxon().parseArray<FriendRequire>(String(response))
                    withContext(Dispatchers.Main){
                        if(arrayFriendRequire==null){
                            callback(listOf())
                            return@withContext
                        }
                        callback(arrayFriendRequire)
                    }
                }catch (e:java.lang.Exception){
                    coroutineScope {
                        withContext(Dispatchers.Main){
                            Toast.makeText(DiacscApplication.context,"get FriendRequest error",Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun getChatLogList(callBack:(List<ChatLog>)->Unit){
        UserInformation.getChatLogList(object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                coroutineScope {
                    val chatLogList=Klaxon().parseArray<ChatLog>(String(response))
                    withContext(Dispatchers.Main){
                        if(chatLogList==null){
                            Toast.makeText(DiacscApplication.context,"ChatLogList",Toast.LENGTH_SHORT).show()
                            return@withContext
                        }
                        callBack(chatLogList)
                    }
                }
            }

            override fun onError() {
                println("error")
            }
        })
    }

    fun getUserName(id:String,callback:(String)->Unit){
        userListLocal.forEach { userNameID ->
            if(userNameID.id==id){
                return callback(userNameID.name)
            }
        }

        UserInformation.getUserName(id,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val result=Klaxon().parse<UserNameID>(String(response))
                    coroutineScope {
                        withContext(Dispatchers.Main){
                            callback(result?.name?:"")
                        }
                    }
                }catch (e:Exception){
                    coroutineScope {
                        withContext(Dispatchers.Main){
                            Toast.makeText(DiacscApplication.context,"get User Name Error",Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onError() {
                println("error")
            }
        })
    }

    fun login(id:String,passwd:String,callBack:(isSuccess:Boolean,id:String?,name:String?)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            UserInformation.login(id,passwd,object :HttpCallbackListener{
                override suspend fun onFinish(response: ByteArray) {
                    try {
                        val isLogin=Klaxon().parse<Login>(String(response))
                        if(isLogin==null)
                            throw java.lang.Exception("isLogin is null")
                        withContext(Dispatchers.Main){
                            callBack(isLogin.isSuccess,isLogin.id,isLogin.name)
                        }
                    }catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(DiacscApplication.context,"login error",Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError() {
                    println("Error")
                }
            })
        }
    }

    //get user messsage list
    fun getUserMessageList(receiverID:String,page:String,pageSize:String,callback:(List<AppMessage>)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            UserInformation.getUserMessageList(receiverID,page,pageSize,object :HttpCallbackListener{
                override suspend fun onFinish(response: ByteArray) {
                    try {
                        val JSONUserMessage=Klaxon().parseArray<AppMessage>(String(response))
                        if(JSONUserMessage==null || JSONUserMessage.isEmpty())
                            return
                        withContext(Dispatchers.Main){
                            callback(JSONUserMessage)
                        }
                    }catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                override fun onError() {
                    println("Error")
                }
            })
        }
    }

    fun changeUserHeadportrait(uri: Uri,callback: () -> Unit){
        UserInformation.changeUserHeadportrait(uri,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<ChangeUserHeadportraitConstruct>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw java.lang.Exception("change headportrait error")
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun agreeAddFriendRequire(friendID: String,callback: () -> Unit){
        UserInformation.agreeAddFriendRequire(friendID,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<AgreeAddFriendRequireConstrut>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw java.lang.Exception("agreeing request of adding friend is error")
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun qrAddFriend(qrAddFriendJSON:QRAddFriendJSON,callback: () -> Unit){
        UserInformation.qrAddFriend(qrAddFriendJSON,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<QRAddfriendConstruct>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw Exception("add friend error")
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun deleteAddFriendRequire(friendID:String,callback:()->Unit){
        UserInformation.deleteAddFriendRequire(friendID,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<DeleteFriendRequireConstruct>(String(response))
                    if (isOk==null||!isOk.isOk){
                        throw Exception("delete request of friend error")
                    }
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun changeUserName(newName:String,callback: ()->Unit){
        UserInformation.changeUserName(newName,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try{
                    val isOk=Klaxon().parse<ChangeUserNameConstruct>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw Exception("change name error")
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }

    fun changeUserPasswd(newPasswd:String,callback: () -> Unit){
        UserInformation.changeUserPasswd(newPasswd,object :HttpCallbackListener{
            override suspend fun onFinish(response: ByteArray) {
                try {
                    val isOk=Klaxon().parse<ChangeUserPasswdContruct>(String(response))
                    if(isOk==null||!isOk.isOk)
                        throw Exception("change passwd error")
                    withContext(Dispatchers.Main){
                        callback()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(DiacscApplication.context,e.stackTraceToString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError() {
                println("Error")
            }
        })
    }


    fun clearUserListLocal(){
        if(!userListLocal.isEmpty())
            userListLocal.clear()
    }
}