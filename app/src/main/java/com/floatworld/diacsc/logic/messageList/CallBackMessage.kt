package com.floatworld.diacsc.logic.messageList

data class CallBackMessage(val messageID:String,val callBack:(messageID:String,newID:String)->Unit)