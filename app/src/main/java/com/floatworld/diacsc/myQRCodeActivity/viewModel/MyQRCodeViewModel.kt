package com.floatworld.diacsc.myQRCodeActivity.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.beust.klaxon.JsonObject
import com.floatworld.diacsc.logic.qrCode.QRCode
import com.floatworld.diacsc.logic.sha.Sha256
import com.floatworld.diacsc.userInformation.CurrentConfig
import com.floatworld.diacsc.userInformation.UserResourceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyQRCodeViewModel:ViewModel() {
    val userImagePainter= mutableStateOf<Painter?>(null)
    val userQRImagePainter=mutableStateOf<Painter?>(null)
    fun getUserImage(){
        UserResourceRepository.getUserImage(CurrentConfig.userID){
            userImagePainter.value=it
        }
    }

    fun getQRCode(){
        CoroutineScope(Dispatchers.IO).launch {
            val jsonObject=JsonObject()
            jsonObject.put("userID",CurrentConfig.userID)
            jsonObject.put("repeatPasswd",Sha256.getSha256(CurrentConfig.passwd))
            val bitmap= QRCode.createQRCode(jsonObject.toJsonString(),500,500)
            if(bitmap==null)
                return@launch
            withContext(Dispatchers.Main){
                userQRImagePainter.value=BitmapPainter(bitmap.asImageBitmap())
            }
        }
    }
}