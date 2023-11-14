package com.floatworld.diacsc.logic.internet

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toFile
import com.floatworld.diacsc.DiacscApplication
import com.floatworld.diacsc.logic.jsonParser.QRAddFriendJSON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.floatworld.diacsc.userInformation.CurrentConfig
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object UserInformation {
    fun login(id:String,passwd: String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/login?id=${id}&passwd=${passwd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getAddFriendRequire(listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_add_friend_require?userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun deleteAddFriendRequire(friendID:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/delete_add_friend_require?" +
                "userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&friendID=${friendID}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun agreeAddFriendRequire(friendID: String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/agree_add_friend_require?" +
                    "userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&friendID=${friendID}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun qrAddFriend(qrAddFriendJSON:QRAddFriendJSON,listener:HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/qr_add_friend?" +
                    "userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&friendID=${qrAddFriendJSON.userID}" +
                    "&repeatPasswd=${qrAddFriendJSON.repeatPasswd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun changeUserName(newName:String,listener:HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch{
            val url=APPWebURL.url+":${APPWebURL.port}/change_user_name?" +
                    "userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&newName=${newName}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun changeUserHeadportrait(uri: Uri,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch{
            val url=APPWebURL.url+":${APPWebURL.port}/upload_headportait"
            val multipartBody=MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userID",CurrentConfig.userID)
                .addFormDataPart("passwd",CurrentConfig.passwd)
                .addFormDataPart("image","headportrait",
                    uri.toFile().asRequestBody()).build()

            val data=AppInternet.getInternetDataPost(url,multipartBody)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun changeUserPasswd(newPasswd:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch{
            val url=APPWebURL.url+":${APPWebURL.port}/change_user_passwd?" +
                    "userID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&newPasswd=${newPasswd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getUserList(userId:String,passwd:String,listener:HttpCallbackListener){
        val job=Job()
        val scope=CoroutineScope(job)
        scope.launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_users_list?id=${userId}&passwd=${passwd}"
            val data=AppInternet.getInternetData(url = url)
            if (!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getChatLogList(listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_chat_log_list?id=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }


    fun getUserName(userId:String,listener: HttpCallbackListener){
        CoroutineScope(Job()).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_user_name?id=${userId}"
            val data=AppInternet.getInternetData(url=url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getIsFriend(id:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_is_friend?userID=${CurrentConfig.userID}&otherID=${id}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getUserImage(id:String,listener:HttpCallbackListener){
        val job=Job()
        val scope=CoroutineScope(job)
        scope.launch {
            val url=APPWebURL.url+":${APPWebURL.port}/image/user/${id}"
            val data=AppInternet.getInternetData(url = url)
            if (!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun addFriendRequest(id:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/add_friend_require?userID=${CurrentConfig.userID}"+
                    "&passwd=${CurrentConfig.passwd}&friendID=${id}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }

    fun getUserMessageList(receiverID:String,page:String,pageSize:String,listener: HttpCallbackListener){
        CoroutineScope(Dispatchers.IO).launch {
            val url=APPWebURL.url+":${APPWebURL.port}/get_user_message_list?"+
                    "ID=${CurrentConfig.userID}&passwd=${CurrentConfig.passwd}&"+
                    "receiverID=${receiverID}&page=${page}&pageSize=${pageSize}"
            val data=AppInternet.getInternetData(url)
            if(!data.status){
                withContext(Dispatchers.Main){
                    Toast.makeText(DiacscApplication.context,data.msg,Toast.LENGTH_LONG).show()
                }
                return@launch
            }
            listener.onFinish(data.data!!)
        }
    }
}