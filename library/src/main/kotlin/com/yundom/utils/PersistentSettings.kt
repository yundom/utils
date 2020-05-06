package com.yundom.utils

interface PersistentSettings {
    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun setInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
    fun setFloat(key: String, value: Float)
    fun getFloat(key: String, defaultValue: Float): Float
    fun setString(key: String, value: String)
    fun getString(key: String, defaultValue: String): String
    fun setObject(key: String, value: Any)
    fun getObject(key: String, cls: Class<*>, defaultValue: Any?): Any?
    fun contains(key: String): Boolean
    fun remove(key: String)
    fun reset()
}
