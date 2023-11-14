package com.floatworld.diacsc.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.JsonObject
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.chatMessage.SendMessage
import com.floatworld.diacsc.logic.internet.MessageLoop
import com.floatworld.diacsc.logic.internet.WSInternet
import com.floatworld.diacsc.logic.internet.WSWebSocketListener
import com.floatworld.diacsc.logic.messageList.CallBackMessage
import com.floatworld.diacsc.logic.messageList.CallBackMessageList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import com.floatworld.diacsc.userInformation.CurrentConfig

class ConnectionService : Service() {
    @Volatile
    private lateinit var webSocket : WebSocket
    private var connectionBinder=ConnectionBinder()

    override fun onBind(intent: Intent): IBinder {
        Log.d("threadTest",Thread.currentThread().name)
        return connectionBinder
    }

    inner class ConnectionBinder:Binder(){
        fun sendMessage(messageID:String,receiverID:String,message:String,callBack:(messageID:String,newID:String)->Unit){
            if(webSocket==null)
                return

            MessageLoop.messageLoop.add(SendMessage(messageID, CurrentConfig.userID,receiverID,message))
            CallBackMessageList.callBackList.add(CallBackMessage(messageID,callBack))
            WSInternet.sendMessage()
        }

        fun setWebSocket(webS:WebSocket){
            webSocket=webS
        }

        fun getWebSocket(): WebSocket? {
            return webSocket
        }

        fun createWS(){
            CoroutineScope(Dispatchers.IO).launch {
                webSocket=WSInternet.create(WSWebSocketListener)
            }
        }
    }
}

