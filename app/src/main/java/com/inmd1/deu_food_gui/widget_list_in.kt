package com.inmd1.deu_food_gui

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.json.JSONObject
import org.json.JSONTokener


class widget_list_in(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    var information: ArrayList<widgetItem>? = null

    fun setData() {
        information = ArrayList()
        var check = PreferenceManager().getString(context, "information")
        //val data = JSONTokener("{\"정보공학관 코너1\":[{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너1\",\"menuName\":\"치즈치킨까스\",\"menuTime\":\"11:00 ~ 15:00\"},{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너1\",\"menuName\":\"치즈치킨까스\",\"menuTime\":\"11:00 ~ 15:00\"}],\"정보공학관 코너3\":[{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너3\",\"menuName\":\"두루치기\",\"menuTime\":\"11:00 ~ 15:00\"},{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너3\",\"menuName\":\"두루치기\",\"menuTime\":\"11:00 ~ 15:00\"}],\"정보공학관 코너4\":[{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너4\",\"menuName\":\"라면\",\"menuTime\":\"11:00 ~ 15:00\"},{\"rk\":null,\"menuDate\":\"2022-03-14\",\"kioskName\":\"정보공학관 코너4\",\"menuName\":\"라면\",\"menuTime\":\"11:00 ~ 15:00\"}]}").nextValue() as JSONObject
        if(check.toString() == "null") {
            information?.add(widgetItem("ERROR","주말 또는 운영을 안합니다"))
        }else{
            var data = JSONTokener(PreferenceManager().getString(context, "information")).nextValue() as JSONObject
            if (data.getString("정보공학관 코너1") != "undefind" && data.getString("정보공학관 코너1") != null
                && data.getString("정보공학관 코너3") == "undefind" && data.getString("정보공학관 코너3") == null) {
                information?.add(widgetItem("코너1", data.getJSONArray("정보공학관 코너1").getJSONObject(0).getString("menuName") ))
                information?.add(widgetItem("코너4","라면"))
            } else if (data.getString("정보공학관 코너1") == "undefind" && data.getString("정보공학관 코너1") == null
                && data.getString("정보공학관 코너3") != "undefind" && data.getString("정보공학관 코너3") != null) {
                information?.add(widgetItem("코너3", data.getJSONArray("정보공학관 코너3").getJSONObject(0).getString("menuName") ))
                information?.add(widgetItem("코너4","라면"))
            } else {
                information?.add(widgetItem("코너1", data.getJSONArray("정보공학관 코너1").getJSONObject(0).getString("menuName") ))
                information?.add(widgetItem("코너3", data.getJSONArray("정보공학관 코너3").getJSONObject(0).getString("menuName") ))
                information?.add(widgetItem("코너4","라면"))
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

    override fun getCount(): Int = information!!.size

    override fun getViewAt(p0: Int): RemoteViews {
        val listviewWidget = RemoteViews(context.packageName, R.layout.item_collection)
        listviewWidget.setTextViewText(R.id.text1, information?.get(p0)?.room)
        listviewWidget.setTextViewText(R.id.text2, information?.get(p0)?.menu)
        val dataIntent = Intent()
        dataIntent.putExtra("item_data", information?.get(p0)?.menu)
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

