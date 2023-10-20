package com.inmd1.deu_food_gui

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import kotlin.collections.ArrayList


class widget_list(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    var Sudeokjeonlist: ArrayList<widgetItem>? = null

    fun setData() {
        Sudeokjeonlist = ArrayList()
        var check = PreferenceManager().getString(context, "Sudeokjeon")
        //val data = JSONTokener("{\"수덕전 코너3\":[{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"두루치기\",\"kioskName\":\"수덕전 코너3\",\"menuTime\":\"11:00 ~ 15:00\"}],\"수덕전 예비용\":[{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"라면\",\"kioskName\":\"수덕전 예비용\",\"menuTime\":\"11:00 ~ 15:00\"},{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"김밥\",\"kioskName\":\"수덕전 예비용\",\"menuTime\":\"11:00 ~ 15:00\"},{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"김밥&라면\",\"kioskName\":\"수덕전 예비용\",\"menuTime\":\"11:00 ~ 15:00\"}],\"수덕전 코너2\":[{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"치킨마요덮밥\",\"kioskName\":\"수덕전 코너2\",\"menuTime\":\"11:00 ~ 15:00\"}],\"수덕전 코너1\":[{\"menuDate\":\"2022-03-11\",\"rk\":null,\"menuName\":\"정식\",\"kioskName\":\"수덕전 코너1\",\"menuTime\":\"11:00 ~ 15:00\"}]}\n").nextValue() as JSONObject
        if(check.toString() == "null") {
            Sudeokjeonlist?.add(widgetItem("ERROR","주말 또는 운영을 안합니다"))
        }else{
            val data = JSONTokener(PreferenceManager().getString(context, "Sudeokjeon")).nextValue() as JSONObject
            if (data.getString("수덕전 코너2") != "undefind" && data.getString("수덕전 코너2") != null
                && data.getString("수덕전 코너3") == "undefind" && data.getString("수덕전 코너3") == null) {
                Sudeokjeonlist?.add(widgetItem("예비용","라면&라면김밥"))
                Sudeokjeonlist?.add(widgetItem("코너1","정식"))
                Sudeokjeonlist?.add(widgetItem("코너2", data.getJSONArray("수덕전 코너2").getJSONObject(0).getString("menuName") ))
            } else if (data.getString("수덕전 코너2") == "undefind" && data.getString("수덕전 코너2") == null
                && data.getString("수덕전 코너3") != "undefind" && data.getString("수덕전 코너3") != null) {
                Sudeokjeonlist?.add(widgetItem("예비용","라면&라면김밥"))
                Sudeokjeonlist?.add(widgetItem("코너1","정식"))
                Sudeokjeonlist?.add(widgetItem("코너3", data.getJSONArray("수덕전 코너3").getJSONObject(0).getString("menuName") ))
            } else {
                Sudeokjeonlist?.add(widgetItem("예비용","라면&라면김밥"))
                Sudeokjeonlist?.add(widgetItem("코너1","정식"))
                Sudeokjeonlist?.add(widgetItem("코너2", data.getJSONArray("수덕전 코너2").getJSONObject(0).getString("menuName") ))
                Sudeokjeonlist?.add(widgetItem("코너3", data.getJSONArray("수덕전 코너3").getJSONObject(0).getString("menuName") ))
            }
        }
    }

    override fun onCreate() {
      setData()
    }

    override fun onDataSetChanged() {
        setData()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = Sudeokjeonlist!!.size

    override fun getViewAt(p0: Int): RemoteViews {
        val listviewWidget = RemoteViews(context.packageName, R.layout.item_collection)
        listviewWidget.setTextViewText(R.id.text1, Sudeokjeonlist?.get(p0)?.room)
        listviewWidget.setTextViewText(R.id.text2, Sudeokjeonlist?.get(p0)?.menu)
        val dataIntent = Intent()
        dataIntent.putExtra("item_data", Sudeokjeonlist?.get(p0)?.menu)
        listviewWidget.setOnClickFillInIntent(R.id.text2, dataIntent)
        return listviewWidget
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}

