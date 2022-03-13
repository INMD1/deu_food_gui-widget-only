package com.inmd1.deu_food_gui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
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
    val format_update = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val client = OkHttpClient()
            val Sudeokjeon_re = Request.Builder().url("https://smart.deu.ac.kr/m/sel_dfood?date=" + format_api.format(date) + "&gubun1=1&gubun2=1").build()
            val information_re = Request.Builder().url("https://smart.deu.ac.kr/m/sel_dfood?date=" + format_api.format(date) + "&gubun1=1&gubun2=2").build()

            client.newCall(Sudeokjeon_re).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    if(json != "{}"){
                        PreferenceManager().setString(context,"Sudeokjeon",json)
                    }else{
                        PreferenceManager().setString(context,"Sudeokjeon","null")
                    }
                }
            })
            client.newCall(information_re).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    if(json != "{}"){
                        PreferenceManager().setString(context,"information",json)
                    }else{
                        PreferenceManager().setString(context,"information","null")
                    }
                    val suserviceIntent = Intent(context, suRemoteViewsService::class.java)
                    val inserviceIntent = Intent(context, inRemoteViewsService::class.java)
                    val widget  = RemoteViews(context.packageName, R.layout.deu_food__widget)
                    widget.setTextViewText(R.id.update_output, format_update.format(date))
                    widget.setRemoteAdapter(R.id.info, inserviceIntent)
                    widget.setRemoteAdapter(R.id.sudack, suserviceIntent)
                    widget.setOnClickPendingIntent(R.id.App_start1, deu20(context))
                    widget.setOnClickPendingIntent(R.id.App_start1, deu20(context))
                    widget.setOnClickPendingIntent(R.id.App_start2, attendance(context))
                    widget.setOnClickPendingIntent(R.id.button_refresh, test(context))
                    appWidgetManager.updateAppWidget(appWidgetId, widget)
                }
            })
        }
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
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

    private fun test (context: Context?): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://google.com"))
        return  PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
    }
    //set code
}

class suRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory  {
        return widget_list(this.applicationContext)
    }
}

class inRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory  {
        return widget_list_in(this.applicationContext)
    }
}