package com.floatworld.diacsc.logic.internet

import android.util.Log
import android.widget.Toast
import com.floatworld.diacsc.DiacscApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

object AppInternet {
    fun getInternetData(url:String):AppInternetMessage {
        try {
            val client=OkHttpClient()
            val request=Request.Builder().
            url(url).
            build()

            val response=client.newCall(request).execute()
            val responseData=response.body?.bytes()
            if (responseData?.size==0)
                return AppInternetMessage(false,null,"response is null")
            return AppInternetMessage(true,responseData)
        }catch (e:Exception){
            e.printStackTrace()
            return AppInternetMessage(false,null,e.stackTraceToString())
        }
    }

    fun getInternetDataPost(url: String,multipartBody:MultipartBody):AppInternetMessage{
        try {
            val client=OkHttpClient()
            val request=Request.Builder().
            url(url).
            post(multipartBody).
            build()

            val response=client.newCall(request).execute()
            val responseData=response.body?.bytes()
            if (responseData?.size==0)
                return AppInternetMessage(false,null,"response is null")
            return AppInternetMessage(true,responseData)
        }catch (e:Exception){
            e.printStackTrace()
            return AppInternetMessage(false,null,e.stackTraceToString())
        }
    }
}