package com.floatworld.diacsc.logic.internet

import android.util.Log
import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import com.floatworld.diacsc.DiacscApplication
import kotlinx.coroutines.Dispatchers
import com.floatworld.diacsc.userInformation.CurrentConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit

object WSInternet {
    @Volatile
    var isConnection=false
    fun create(webSocketListener: WebSocketListener): WebSocket {
        val client=OkHttpClient.Builder().pingInterval(40, TimeUnit.SECONDS).build()
        val wsUrl="${APPWebURL.wsUrl}:${APPWebURL.port}/login?id=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}"
        val request=Request.Builder().url(wsUrl).build()
        val webSocket=client.newWebSocket(request,webSocketListener)
        return webSocket
    }

    fun sendMessage(){
        if(DiacscApplication.binder.getWebSocket()==null)
            return
        MessageLoop.messageLoop.forEach {sendMessage ->
            val jsonObject= JsonObject()
            jsonObject.put("messageID",sendMessage.messageID)
            jsonObject.put("senderID", sendMessage.senderID)
            jsonObject.put("receiverID",sendMessage.receiverID)
            jsonObject.put("message",sendMessage.message)
            val sendMessage=jsonObject.toJsonString()
            DiacscApplication.binder.getWebSocket()!!.send(sendMessage)
        }

    }
}