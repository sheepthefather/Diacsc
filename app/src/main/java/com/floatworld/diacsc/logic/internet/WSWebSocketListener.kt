package com.floatworld.diacsc.logic.internet

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.jsonParser.MessageJsonParser
import com.floatworld.diacsc.logic.jsonParser.MessageModel
import com.floatworld.diacsc.logic.messageList.AppMessage
import com.floatworld.diacsc.logic.messageList.AppMessageList
import com.floatworld.diacsc.logic.messageList.CallBackMessageList
import com.floatworld.diacsc.userInformation.CurrentConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.http2.Http2Reader
import okio.ByteString
import java.util.Date

object WSWebSocketListener:WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        WSInternet.isConnection=true

        MessageLoop.messageLoop.forEach {
            WSInternet.sendMessage()
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        WSInternet.isConnection=false
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            DiacscApplication.binder.setWebSocket(WSInternet.create(WSWebSocketListener))
            withContext(Dispatchers.Main){
                Toast.makeText(DiacscApplication.context,"WS Connection failure",Toast.LENGTH_LONG).show()
                Log.d("Failure ID:",t.message?:"")
            }
        }

    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        WSInternet.isConnection=false
        CoroutineScope(Job()).launch {
            withContext(Dispatchers.Main){
                Toast.makeText(DiacscApplication.context,"WS Connection close",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        var messageJson: MessageModel?
        try {
            messageJson=MessageJsonParser.parserMessageJson(text) //json parser
        }catch (e:Exception){
            CoroutineScope(Dispatchers.Main).launch{
                Toast.makeText(DiacscApplication.context,"server Ws Message Error",Toast.LENGTH_SHORT).show()
            }
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            AppMessageList.list.forEach{callback->
                if(messageJson==null ||
                    (messageJson.errorStructure==null&&messageJson.successStructure==null&&messageJson.callBackStructure==null)){
                    Log.d("Service Error","server return json error")
                    return@launch
                }
                if(!messageJson.status){
                    Log.e("Service Error",messageJson.errorStructure?.errorID+":${messageJson.errorStructure?.errorMessage}")
                    //if error code is passwd error
                    if(messageJson.errorStructure!!.errorID=="5"){
                        Log.e("Service Error","Passwd Error")
                        val intent=Intent("com.floatworld.diacsc.FORCE_OFFLINE")
                        intent.setPackage(DiacscApplication.context.packageName)
                        DiacscApplication.context.sendBroadcast(intent)

                    }
                    return@launch
                }
                //message send successly
                if(messageJson.callBackStructure!=null){
                    MessageLoop.messageLoop.removeIf {sendMessage->
                        sendMessage.messageID== messageJson.callBackStructure!!.callBackMessageID
                    }
                    var i=0
                    while (i<CallBackMessageList.callBackList.size){
                        val callBackMessage=CallBackMessageList.callBackList[i]
                        if(callBackMessage.messageID==messageJson.callBackStructure!!.callBackMessageID){
                            callBackMessage.callBack(messageJson.callBackStructure!!.callBackMessageID,messageJson.callBackStructure!!.newID)
                            CallBackMessageList.callBackList.removeAt(i)
                            i--
                        }
                        i++
                    }
                    return@launch
                }


                if(messageJson.successStructure==null){
                    Log.e("Service Error","Service successStructure error")
                    return@launch
                }

                val appMessage=AppMessage(
                    messageJson.successStructure!!.ID,
                    messageJson.successStructure!!.senderID,
                    messageJson.successStructure!!.receiverID, messageJson.successStructure!!.message,messageJson.successStructure!!.date
                )
                if(appMessage.senderID!= CurrentConfig.userID && appMessage.receiverID!= CurrentConfig.userID){
                    Log.e("Service Error","Error senderID")
                    return@launch
                }
                //call callback
                callback(appMessage,true)
            }
        }
    }
}