package com.floatworld.diacsc

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.floatworld.diacsc.loginActivity.LoginActivity

open class BaseActivity: ComponentActivity() {
    lateinit var receiver:ForOffLineReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter=IntentFilter()
        intentFilter.addAction("com.floatworld.diacsc.FORCE_OFFLINE")
        receiver=ForOffLineReceiver()
        registerReceiver(receiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    inner class ForOffLineReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            ActivityCollector.finishAll()
            val i=Intent(context,LoginActivity::class.java)
            context.startActivity(i)
        }
    }
}