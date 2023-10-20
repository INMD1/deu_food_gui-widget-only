package com.inmd1.deu_food_gui

import android.content.Context
import android.content.SharedPreferences




class PreferenceManager {
    val PREFERENCES_NAME = "rebuild_preference"
    private val DEFAULT_VALUE_STRING = ""
    private val DEFAULT_VALUE_INT = -1

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    /**
     * int 값 저장
     * @param context
     * @param key
     * @param value
     */
    fun setInt(context: Context, key: String?, value: Int) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }


    /**
     * String 값 저장
     * @param context
     * @param key
     * @param value
     * @return
     */
    fun setString(context: Context, key: String?, value: String?): String? {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
        return key
    }

    /**
     * int 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getInt(context: Context, key: String?): Int {
        val prefs = getPreferences(context)
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    /**
     * String 값 로드
     * @param context
     * @param key
     * @return
     */
    fun getString(context: Context, key: String?): String? {
        val prefs = getPreferences(context)
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }


    /**
     * 모든 저장 데이터 삭제
     * @param context
     */
    fun clear(context: Context) {
        val prefs = getPreferences(context)
        val edit = prefs.edit()
        edit.clear()
        edit.commit()
    }
}