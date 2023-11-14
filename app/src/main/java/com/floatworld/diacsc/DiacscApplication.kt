package com.floatworld.diacsc

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.compose.material3.contentColorFor
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import coil.compose.ImagePainter
import com.floatworld.diacsc.connectionActivity.ConnectionActivity
import com.floatworld.diacsc.logic.messageList.AppMessage
import com.floatworld.diacsc.logic.messageList.AppMessageList

import com.floatworld.diacsc.service.ConnectionService
import com.floatworld.diacsc.userInformation.UserInformationDepository
import com.floatworld.diacsc.userInformation.UserResourceRepository

class DiacscApplication:Application(),Application.ActivityLifecycleCallbacks {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        @Volatile
        var isServerRunning=false
        lateinit var binder:ConnectionService.ConnectionBinder

        var isForeground=false

        private val connection=object :ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binder=service as ConnectionService.ConnectionBinder
                binder.createWS()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                TODO("Not yet implemented")
            }
        }

        fun createServer(){
            if(isServerRunning)
                return
            isServerRunning=true
            val intent=Intent(context,ConnectionService::class.java)
            context.bindService(intent,connection,Context.BIND_AUTO_CREATE)
        }

        fun stopServer(){
            if(!isServerRunning)
                return
            binder.getWebSocket()?.close(1000,"")
            isServerRunning=false
            context.unbindService(connection)
        }

    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        context=applicationContext

        createNotificationChannel()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//        Log.d("application status:","created")
    }

    override fun onActivityStarted(p0: Activity) {
//        Log.d("application status:","started")
    }

    override fun onActivityResumed(p0: Activity) {
        isForeground=true
        AppMessageList.list.remove(::appMessageCallback)

    }

    override fun onActivityPaused(p0: Activity) {
        isForeground=false
        AppMessageList.list.add(::appMessageCallback)
    }

    override fun onActivityStopped(p0: Activity) {
//        Log.d("application status:","stopped")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
//        Log.d("application status:","save")
    }

    override fun onActivityDestroyed(p0: Activity) {
//        Log.d("application status:","destroy")
    }

    //create notification
    private fun appMessageCallback(appMessage: AppMessage, isSend: Boolean){
        if(!isSend)
            return
        UserInformationDepository.getUserName(appMessage.senderID){userName->
            UserResourceRepository.getUserImageAsBitMap(appMessage.senderID){bitmap->
                val intent=Intent(context,ConnectionActivity::class.java)
                intent.apply {
                    putExtra("personID",appMessage.senderID)
                    flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent=PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                val notification= NotificationCompat.Builder(context,"message").
                setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(bitmap).
                setContentIntent(pendingIntent).
                setContentTitle(userName).
                setContentText(appMessage.message).
                setPriority(NotificationCompat.PRIORITY_DEFAULT).build()

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@getUserImageAsBitMap
                }
                NotificationManagerCompat.from(context).notify(appMessage.senderID.toInt(10),notification)
            }
        }
    }

    fun createNotificationChannel(){
        val name="user's message"
        val descriptionText="this is a message of other user"
        val importance=NotificationManager.IMPORTANCE_HIGH
        val channel=NotificationChannel("message",name,importance).apply {
            description=descriptionText
        }

        val notificationManager:NotificationManager=
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}