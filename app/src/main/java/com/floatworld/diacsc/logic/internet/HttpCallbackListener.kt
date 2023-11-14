package com.floatworld.diacsc.logic.internet

import java.io.InputStream

interface HttpCallbackListener {
    suspend fun onFinish(response: ByteArray)
    fun onError()
}