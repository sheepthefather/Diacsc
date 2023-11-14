package com.floatworld.diacsc.userInformation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.internet.HttpCallbackListener
import com.floatworld.diacsc.logic.internet.UserInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File

object UserResourceRepository {
    fun getUserImage(id:String,callback:(Painter)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            //get Image in storage
            val data= getImageInStorage(id)
            if(data!=null){
                val bitmap=BitmapFactory.decodeByteArray(data,0,data.size)
                val imageBitmap=bitmap.asImageBitmap()
                val BitmapPainter=BitmapPainter(imageBitmap)
                coroutineScope {
                    withContext(Dispatchers.Main){
                        callback(BitmapPainter)
                    }
                }
                return@launch
            }

            //get Image in internet
            UserInformation.getUserImage(id,object :HttpCallbackListener{
                override suspend fun onFinish(response: ByteArray) {
                    //file save to the respository
                    savaImageInStorage(id,response)

                    val bitmap=BitmapFactory.decodeByteArray(response,0,response.size)
                    val imageBitmap=bitmap.asImageBitmap()
                    val bitmapPainter=BitmapPainter(imageBitmap)
                    //callback in main thread
                    coroutineScope {
                        withContext(Dispatchers.Main){
                            callback(bitmapPainter)
                        }
                    }
                }

                override fun onError() {
                    println("Error")
                }
            })
        }
    }

    fun getUserImageAsBitMap(id: String,callback: (Bitmap) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            //get Image in storage
            val data= getImageInStorage(id)
            if(data!=null){
                val bitmap=BitmapFactory.decodeByteArray(data,0,data.size)
                coroutineScope {
                    withContext(Dispatchers.Main){
                        callback(bitmap)
                    }
                }
                return@launch
            }

            //get Image in internet
            UserInformation.getUserImage(id,object :HttpCallbackListener{
                override suspend fun onFinish(response: ByteArray) {
                    //file save to the respository
                    savaImageInStorage(id,response)

                    val bitmap=BitmapFactory.decodeByteArray(response,0,response.size)
                    //callback in main thread
                    coroutineScope {
                        withContext(Dispatchers.Main){
                            callback(bitmap)
                        }
                    }
                }

                override fun onError() {
                    println("Error")
                }
            })
        }
    }

    fun savaImageInStorage(id: String,data:ByteArray){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var file= DiacscApplication.context.getExternalFilesDir("userImage")
                file=File(file,id)
                if (file.exists())
                    file.delete()
                if(!file.createNewFile()){
                    throw java.lang.Exception("Create File Error")
                }

                val writeer= BufferedOutputStream(file.outputStream())
                writeer.use {
                    it.write(data)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,e.stackTraceToString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun getImageInStorage(id:String):ByteArray?{
        try {
            var file= DiacscApplication.context.getExternalFilesDir("userImage")
            file=File(file,id)
            if(!file.exists())
                return null
            val reader=BufferedInputStream(file.inputStream())
            reader.use {
                return it.readBytes()
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(DiacscApplication.context,e.stackTraceToString(), Toast.LENGTH_SHORT).show()
            }
        }
        return null
    }

    suspend fun clearUserImage(id:String){
        try{
            var file= DiacscApplication.context.getExternalFilesDir("userImage")
            file=File(file,id)
            if (!file.exists())
                return
            if(!file.delete())
                throw Exception("清除头像缓存错误")
            return
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(DiacscApplication.context,e.stackTraceToString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getBackground():Painter?{
        try {
            var file= DiacscApplication.context.getExternalFilesDir("background")
            file=File(file,"background")
            if(!file.exists())
                return null
            val reader=BufferedInputStream(file.inputStream())
            reader.use {
                val data=it.readBytes()
                return BitmapPainter(BitmapFactory.decodeByteArray(data,0,data.size).asImageBitmap())
            }
        }catch (e:Exception){
            Toast.makeText(DiacscApplication.context,e.stackTraceToString(), Toast.LENGTH_SHORT).show()
        }
        return null
    }

    suspend fun saveBackground(data: ByteArray){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var file = DiacscApplication.context.getExternalFilesDir("background")
                file = File(file, "background")
                if (file.exists())
                    file.delete()
                if (!file.createNewFile()) {
                    throw java.lang.Exception("Create File Error")
                }

                val writeer = BufferedOutputStream(file.outputStream())
                writeer.use {
                    it.write(data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        DiacscApplication.context,
                        e.stackTraceToString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}