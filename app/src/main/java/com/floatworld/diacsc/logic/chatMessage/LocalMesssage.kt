package com.floatworld.diacsc.logic.chatMessage

import com.floatworld.diacsc.logic.messageList.AppMessage

class LocalMesssage(var ID:String,val senderID:String,val receiverID:String,val message:String,val date:String,var isSend:Boolean) {
    constructor(appMessage: AppMessage,isSend: Boolean):this(appMessage.ID,
        appMessage.senderID,appMessage.receiverID,appMessage.message,appMessage.date,isSend)
}