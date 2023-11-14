package com.floatworld.diacsc.logic.internet

import android.widget.Toast
import com.floatworld.diacsc.DiacscApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object InternetOperation {
    fun register(name:String,passwd:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/register?name=${name}&passwd=${passwd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg, Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }
}