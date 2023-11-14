package com.floatworld.diacsc.logic.messageList

import com.floatworld.diacsc.logic.chatMessage.LocalMesssage

object AppMessageList {
    val list=mutableListOf<(appMessage: AppMessage,isSend:Boolean)->Unit>()
}