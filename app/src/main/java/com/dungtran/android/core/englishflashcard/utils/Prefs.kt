package com.dungtran.android.core.englishflashcard.utils

import android.content.Context

object Prefs {
    private val prefs = "APP_PREFS"

    val IS_LOGIN = "isLogin"

    fun setIsLogin(context: Context, value: Boolean) {
        save(context, IS_LOGIN, value)
    }

    fun isLogin(context: Context): Boolean {
        return get(context, IS_LOGIN, false)
    }

    fun save(context: Context?, key: String, value: String) {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun save(context: Context?, key: String, value: Float) {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putFloat(key, value)
        editor?.apply()
    }

    fun save(context: Context?, key: String, value: Long) {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putLong(key, value)
        editor?.apply()
    }

    fun save(context: Context?, key: String, value: Int) {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun save(context: Context?, key: String, value: Boolean) {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun get(context: Context?, key: String, default: String): String {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        return prefs?.getString(key, default).toString()
    }

    fun get(context: Context?, key: String, default: Float): Float {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        return prefs!!.getFloat(key, default)
    }

    fun get(context: Context?, key: String, default: Long): Long {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE) ?: return default
        return prefs.getLong(key, default)
    }

    fun get(context: Context?, key: String, default: Int): Int {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE) ?: return default
        return prefs.getInt(key, default)
    }

    fun get(context: Context?, key: String, default: Boolean): Boolean {
        val prefs = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        return prefs!!.getBoolean(key, default)
    }

    fun clear(context: Context?) {
        val sharedPreferences = context?.getSharedPreferences(prefs, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}