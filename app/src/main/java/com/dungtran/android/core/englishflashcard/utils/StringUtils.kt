package com.dungtran.android.core.englishflashcard.utils

import com.google.gson.GsonBuilder
import java.lang.reflect.Type
import java.util.regex.Pattern

object StringUtils {
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @JvmStatic
    fun toJson(source: Any): String {
        return gson.toJson(source)
    }

    @JvmStatic
    fun <T> parseJson(source: String, clazz: Class<T>): T {
        return gson.fromJson(source, clazz)
    }

    @JvmStatic
    fun <T> parseJson(source: String, type: Type): T {
        return gson.fromJson(source, type)
    }

    @JvmStatic
    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val emailPattern = Pattern.compile(emailRegex)
        val emailMatcher = emailPattern.matcher(email)
        return emailMatcher.matches()
    }

    @JvmStatic
    fun validatePassword(password: String): Boolean {
        val regex = "^.{8,}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
    @JvmStatic
    fun getLevel(value: Float): String {
        return when (value) {
            in 0f..0.4f -> "Beginner"
            in 0.4f..0.7f -> "Intermediate"
            in 0.7f..1f -> "Advanced"
            else -> "Beginner"
        }
    }

}