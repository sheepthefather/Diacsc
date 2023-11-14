package com.floatworld.diacsc.logic.jsonParser

import com.beust.klaxon.Klaxon

object MessageJsonParser {
    fun parserMessageJson(response:String): MessageModel? {
        val result=Klaxon().parse<MessageModel>(response)
        return result
    }
}