package com.floatworld.diacsc.logic.jsonParser

data class MessageModel (
    val status: Boolean,
    val errorStructure: ErrorStructure? = null,
    val successStructure: SuccessStructure? = null,
    val callBackStructure:CallBackStructure? = null
){
    data class ErrorStructure (
        val errorID: String,
        val errorMessage: String
    )

    data class SuccessStructure (
        val ID:String,
        val message: String,
        val receiverID: String,
        val senderID: String,
        val date:String
    )

    data class CallBackStructure(
        val callBackMessageID:String,
        val newID:String
    )
}
