package com.floatworld.diacsc.logic.qrCode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Hashtable

class QRCode {
    companion object{
        fun createQRCode(codeString: String,width:Int,height:Int):Bitmap?{
            try {
                if(codeString.isEmpty()||width<=0||height<=0)
                    return null
                val hashTable=Hashtable<EncodeHintType,String>()
                hashTable.put(EncodeHintType.CHARACTER_SET,"utf-8");  //设置字符转码格式
                hashTable.put(EncodeHintType.ERROR_CORRECTION,"H");   //设置容错级别
                hashTable.put(EncodeHintType.MARGIN,"2"); //设置空白边距

                val bitMatrix=QRCodeWriter().encode(codeString, BarcodeFormat.QR_CODE,width,height,hashTable)
                val pixel= IntArray(height*width){
                    0
                }
                for (i in 0 until height){
                    for (j in 0 until width){
                        if (bitMatrix.get(j,i)){
                            pixel[i*width+j]=Color.BLACK
                        }else{
                            pixel[i*width+j]=Color.WHITE
                        }
                    }
                }

                val bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
                bitmap.setPixels(pixel,0,width,0,0,width,height)
                return bitmap
            }catch (e:Exception){
                return null
            }
        }
    }
}