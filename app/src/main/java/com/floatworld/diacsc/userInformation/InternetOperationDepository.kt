package com.floatworld.diacsc.userInformation

import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.internet.HttpCallbackListener
import com.floatworld.diacsc.logic.internet.InternetOperation
import com.floatworld.diacsc.logic.jsonParser.Login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object InternetOperationDepository {
    fun register(name:String,passwd:String,callBack:(status:Boolean,id:String?,name: String?)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            InternetOperation.register(name,passwd,object :HttpCallbackListener{
                override suspend fun onFinish(response: ByteArray) {
                    try {
                        val data=Klaxon().parse<Login>(String(response))
                        if(data==null)
                            throw Exception("register data==null")
                        callBack(data.isSuccess,data.id,data.name)
                    }catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(DiacscApplication.context,"register error", Toast.LENGTH_LONG).show()
                            e.printStackTrace()
                        }
                    }

                }

                override fun onError() {
                    print("Error")
                }
            })
        }
    }
}