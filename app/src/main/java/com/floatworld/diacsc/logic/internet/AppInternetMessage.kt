package com.floatworld.diacsc.logic.internet

import java.io.InputStream

//true is success,flase is error
data class AppInternetMessage(val status:Boolean,val data:ByteArray?,val msg:String?=null)
