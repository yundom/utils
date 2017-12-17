package com.mojopot.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Created by yundom on 27/09/2017.
 */

class PersistentSettings(private val preferences: SharedPreferences) {
    fun setBoolean(key: String, value: Boolean) {
        synchronized(preferences) {
            preferences.edit().apply {
                putBoolean(key, value)
                apply()
            }
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        synchronized(preferences) {
            return preferences.getBoolean(key, defaultValue)
        }
    }

    fun setInt(key: String, value: Int) {
        synchronized(preferences) {
            preferences.edit().apply {
                putInt(key, value)
                apply()
            }
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        synchronized(preferences) {
            return preferences.getInt(key, defaultValue)
        }
    }

    fun setFloat(key: String, value: Float) {
        synchronized(preferences) {
            preferences.edit().apply {
                putFloat(key, value)
                apply()
            }
        }
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        synchronized(preferences) {
            return preferences.getFloat(key, defaultValue)
        }
    }

    fun setString(key: String, value: String) {
        synchronized(preferences) {
            preferences.edit().apply {
                putString(key, value)
                apply()
            }
        }
    }

    fun getString(key: String, defaultValue: String): String {
        synchronized(preferences) {
            return preferences.getString(key, defaultValue)
        }
    }

    fun setObject(key: String, value: Any) {
        synchronized(preferences) {
            preferences.edit().apply {
                val gson = Gson()
                val json = gson.toJson(value)
                putString(key, json)
                apply()
            }
        }
    }

    fun getObject(key: String, cls: Class<*>, defaultValue: Any?): Any? {
        synchronized(preferences) {
            var value = defaultValue
            var json = preferences.getString(key, null)
            json?.let {
                val gson = Gson()
                try {
                    value = gson.fromJson(json, cls)
                } catch (e: JsonSyntaxException) {
                    throw IllegalStateException("Couldn't interpret stored object state")
                }
            }
            return value
        }
    }

    fun contains(key: String): Boolean {
        synchronized(preferences) {
            return preferences.contains(key)
        }
    }

    fun remove(key: String) {
        synchronized(preferences) {
            preferences.edit().apply {
                remove(key)
                apply()
            }
        }
    }

    fun reset() {
        synchronized(preferences) {
            preferences.edit().apply {
                clear()
                apply()
            }
        }
    }
}