package com.floatworld.diacsc.userInformation

import android.content.Context
import com.floatworld.diacsc.DiacscApplication

object LocalInformationRepository {
    fun saveUserInfo(id:String,name:String,passwd:String){
        val editor=
            DiacscApplication.context.getSharedPreferences("userData", Context.MODE_PRIVATE).edit()
        editor.apply {
            putString("ID",id)
            putString("name",name)
            putString("passwd",passwd)
        }
        editor.apply()
    }

    fun getUserInfo(): UserInfo {
        val refs=
            DiacscApplication.context.getSharedPreferences("userData", Context.MODE_PRIVATE)
        val id=refs.getString("ID","")
        val name=refs.getString("name","")
        val passwd=refs.getString("passwd","")
        if(id==null||name==null||passwd==null)
            return UserInfo("","","")
        return UserInfo(id,name,passwd)
    }

    class UserInfo(val id:String,val name: String,val passwd: String)
}