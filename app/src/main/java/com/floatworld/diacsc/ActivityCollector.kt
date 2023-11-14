package com.floatworld.diacsc

import android.app.Activity

object ActivityCollector {
    private val activitys=ArrayList<Activity>()
    fun addActivity(activity: Activity){
        activitys.add(activity)
    }

    fun removeActivity(activity: Activity){
        activitys.remove(activity)
    }

    fun finishAll(){
        for(activity in activitys){
            if(!activity.isFinishing)
                activity.finish()
        }
        activitys.clear()
    }
}