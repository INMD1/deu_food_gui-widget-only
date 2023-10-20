package com.inmd1.deu_food_gui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class deu_food_Widget : AppWidgetProvider() {
    val date = Date()
    val format_api = SimpleDateFormat("yyyyMMdd")
    val format_update = SimpleDateFormat("HH:mm:ss")
    val REFRESH_ACTION = "com.inmd1.deu_food_gui.REFRESH_ACTION"

    @SuppressLint("SuspiciousIndentation")
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            val client = OkHttpClient()
            val Sudeokjeon_re = Request.Builder().url("https://smart.deu.ac.kr/m/sel_dfood?date=" + format_api.format(date) + "&gubun1=1&gubun2=1").build()
            val information_re = Request.Builder().url("https://smart.deu.ac.kr/m/sel_dfood?date=" + format_api.format(date) + "&gubun1=1&gubun2=2").build()
                    client.newCall(Sudeokjeon_re).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            println(e)
                        }
                        override fun onResponse(call: Call, response: Response) {
                            val json = response.body?.string()
                            if(json != "{}"){
                                PreferenceManager().setString(context,"Sudeokjeon",json)
                            }else{
                                PreferenceManager().setString(context,"Sudeokjeon","null")
                            }
                            val widget  = RemoteViews(context.packageName, R.layout.deu_food__widget)
                            appWidgetManager.updateAppWidget(appWidgetId, widget)
                            println("수덕전 정보불려옴")
                        }
                    })
                    client.newCall(information_re).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            println(e)
                        }
                        override fun onResponse(call: Call, response: Response) {
                            val json = response.body?.string()
                            if(json != "{}"){
                                PreferenceManager().setString(context,"information",json)
                            }else{
                                PreferenceManager().setString(context,"information","null")
                            }
                            println("정보관 정보 불려오고 리스트 새로 고침함")
                            val inserviceIntent = Intent(context, inRemoteViewsService::class.java)
                            val suserviceIntent = Intent(context, suRemoteViewsService::class.java)

                            val refreshIntent = Intent(context, deu_food_Widget::class.java)
                            refreshIntent.setAction(REFRESH_ACTION)
                            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                            val pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_IMMUTABLE)

                            val widget  = RemoteViews(context.packageName, R.layout.deu_food__widget)
                            widget.setTextViewText(R.id.update_output, format_update.format(date))
                                //처음 초기 설정
                                widget.setRemoteAdapter(R.id.info, inserviceIntent)
                                widget.setOnClickPendingIntent(R.id.App_start1, deu20(context))
                                widget.setOnClickPendingIntent(R.id.App_start2, attendance(context))
                                widget.setOnClickPendingIntent(R.id.button_refresh, pendingIntent)
                                widget.setRemoteAdapter(R.id.sudack, suserviceIntent)

                            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.info);
                            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.sudack);
                            appWidgetManager.updateAppWidget(appWidgetId, widget)
                        }
                    })
            }
    }


    override fun onEnabled(context: Context) {

    }

    override fun onReceive(context: Context, intent: Intent) {
        //버튼을 누루면 동작함
        super.onReceive(context, intent)
        if (intent.action.equals(REFRESH_ACTION)) {
            val extras = intent.getExtras();
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisAppWidget = ComponentName(context.packageName, deu_food_Widget::class.java.getName())
                val appWidgetIds: IntArray = appWidgetManager.getAppWidgetIds(thisAppWidget)
                onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds)
        }
    }

    override fun onDisabled(context: Context) {
        //삭제 되었을때 0으로 세팅을 해서 처음부터 다시 돌아감
        println("삭제됨")
        //PreferenceManager().setInt(context,"Widget_add",0)
    }

    //앱 이동 코드
    private fun deu20 (context: Context?): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW).setClassName("com.libeka.attendance.ucheckplusstud_dongeui","com.libeka.attendance.ucheckplusstud_dongeui.Activity.IntroActivity")
        return  PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun attendance (context: Context?): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW).setClassName("kr.ac.deu.mobileid","com.lotecs.mobileid.LoginActivity")
        return  PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
    }
}

//한번씩 발생하는 함수
class suRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory  {
        println("suRemoteViewsService")
        return widget_list(this.applicationContext)
    }
}

class inRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory  {
        println("inRemoteViewsService")
        return widget_list_in(this.applicationContext)
    }
}