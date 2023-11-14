package com.floatworld.diacsc.logic.sha

import java.security.MessageDigest
import javax.crypto.Mac

object Sha256 {
    fun getSha256(data:String):String{
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(data.toByteArray())
        var sha256=""
        for(i in 0 until bytes.size){
            val char: UByte =bytes[i].toUByte()
            sha256+=if(char.toString(16).length<2) "0"+char.toString(16) else char.toString(16)
        }
        return sha256
    }
}