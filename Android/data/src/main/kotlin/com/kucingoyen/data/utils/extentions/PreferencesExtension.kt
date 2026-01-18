package com.kucingoyen.data.utils.extentions

import android.content.SharedPreferences

/**
 * @throws UnsupportedOperationException if supplied value not matched
 */
@Throws(UnsupportedOperationException::class)
internal fun SharedPreferences.update(vararg values: Pair<Any, String>) {
    val editor = this.edit()
    values.forEach {
        when (it.first) {
            is Int -> editor.putInt(it.second, it.first as Int)
            is Boolean -> editor.putBoolean(it.second, it.first as Boolean)
            is Float -> editor.putFloat(it.second, it.first as Float)
            is Long -> editor.putLong(it.second, it.first as Long)
            is String -> editor.putString(it.second, it.first as String)
            else -> throw UnsupportedOperationException("unsupported type of value extension")
        }
    }
    editor.apply()
}

/**
 * How to use:
 *
 * key: String
 * value: Int, Boolean, Float, Long, String
 *
 * @throws UnsupportedOperationException if supplied value not matched
 */
@Throws(UnsupportedOperationException::class)
internal fun SharedPreferences.update(value: Pair<Any, String>) {
    val editor = this.edit()
    when (value.first) {
        is Int -> editor.putInt(value.second, value.first as Int)
        is Boolean -> editor.putBoolean(value.second, value.first as Boolean)
        is Float -> editor.putFloat(value.second, value.first as Float)
        is Long -> editor.putLong(value.second, value.first as Long)
        is String -> editor.putString(value.second, value.first as String)
        else -> throw UnsupportedOperationException("unsupported type of value extension")
    }
    editor.apply()
}

internal fun SharedPreferences.remove(vararg keys: String): Unit = edit().also { editor ->
    keys.forEach { key -> editor.remove(key) }
}.apply()
