package com.yundom.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class PersistentSettingsImpl(private val preferences: SharedPreferences) : PersistentSettings {
    override fun setBoolean(key: String, value: Boolean) {
        synchronized(preferences) {
            preferences.edit().apply {
                putBoolean(key, value)
                apply()
            }
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        synchronized(preferences) {
            return preferences.getBoolean(key, defaultValue)
        }
    }

    override fun setInt(key: String, value: Int) {
        synchronized(preferences) {
            preferences.edit().apply {
                putInt(key, value)
                apply()
            }
        }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        synchronized(preferences) {
            return preferences.getInt(key, defaultValue)
        }
    }

    override fun setFloat(key: String, value: Float) {
        synchronized(preferences) {
            preferences.edit().apply {
                putFloat(key, value)
                apply()
            }
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        synchronized(preferences) {
            return preferences.getFloat(key, defaultValue)
        }
    }

    override fun setString(key: String, value: String) {
        synchronized(preferences) {
            preferences.edit().apply {
                putString(key, value)
                apply()
            }
        }
    }

    override fun getString(key: String, defaultValue: String): String {
        synchronized(preferences) {
            return preferences.getString(key, defaultValue)
        }
    }

    override fun setObject(key: String, value: Any) {
        synchronized(preferences) {
            preferences.edit().apply {
                val gson = Gson()
                val json = gson.toJson(value)
                putString(key, json)
                apply()
            }
        }
    }

    override fun getObject(key: String, cls: Class<*>, defaultValue: Any?): Any? {
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

    override fun contains(key: String): Boolean {
        synchronized(preferences) {
            return preferences.contains(key)
        }
    }

    override fun remove(key: String) {
        synchronized(preferences) {
            preferences.edit().apply {
                remove(key)
                apply()
            }
        }
    }

    override fun reset() {
        synchronized(preferences) {
            preferences.edit().apply {
                clear()
                apply()
            }
        }
    }
}
