package com.yundom.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test

class PersistentSettingsImplTest : BaseRobolectricTestCase() {
    companion object {
        const val TEST_KEY = "TEST_KEY"
        const val FILE_NAME = "filename"
    }

    private lateinit var sharedPreference: SharedPreferences

    private lateinit var persistentSettings: PersistentSettings

    @Before
    fun setUp() {
        sharedPreference = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        persistentSettings = PersistentSettingsImpl(sharedPreference)
    }

    @Test
    fun setBoolean() {
        persistentSettings.setBoolean(TEST_KEY, true)

        assertTrue(sharedPreference.getBoolean(TEST_KEY, false))
    }

    @Test
    fun getBoolean() {
        val editor = sharedPreference.edit()
        editor.putBoolean(TEST_KEY, true)
        editor.commit()

        val result = persistentSettings.getBoolean(TEST_KEY, false)

        assertTrue(result)
    }

    @Test
    fun setInt() {
        persistentSettings.setInt(TEST_KEY, 123)

        assertEquals(123, sharedPreference.getInt(TEST_KEY, 0))
    }

    @Test
    fun getInt() {
        val editor = sharedPreference.edit()
        editor.putInt(TEST_KEY, 123)
        editor.commit()

        assertEquals(123, persistentSettings.getInt(TEST_KEY, 0))
    }

    @Test
    fun setFloat() {
        persistentSettings.setFloat(TEST_KEY, 0.123f)

        assertEquals(0.123f, sharedPreference.getFloat(TEST_KEY, 0f))
    }

    @Test
    fun getFloat() {
        val editor = sharedPreference.edit()
        editor.putFloat(TEST_KEY, 0.123f)
        editor.commit()

        assertEquals(0.123f, persistentSettings.getFloat(TEST_KEY, 0f))
    }

    @Test
    fun setString() {
        persistentSettings.setString(TEST_KEY, "HELLO")

        assertEquals("HELLO", sharedPreference.getString(TEST_KEY, ""))
    }

    @Test
    fun getString() {
        val editor = sharedPreference.edit()
        editor.putString(TEST_KEY, "HELLO")
        editor.commit()

        assertEquals("HELLO", persistentSettings.getString(TEST_KEY, ""))
    }

    @Test
    fun setObject() {
        persistentSettings.setObject(TEST_KEY, ClassForTest())

        assertEquals(classForTestJson(), sharedPreference.getString(TEST_KEY, ""))
    }

    @Test
    fun getObject() {
        val testObject = ClassForTest()
        val editor = sharedPreference.edit()
        editor.putString(TEST_KEY, classForTestJson())
        editor.commit()

        val result = persistentSettings.getObject(TEST_KEY, ClassForTest::class.java, null)

        assertTrue(result is ClassForTest)
        val obj = result as ClassForTest
        assertEquals(obj.property, testObject.property)
        assertEquals(obj.innerClassForTest.property, testObject.innerClassForTest.property)
    }

    @Test
    fun contains() {
        val editor = sharedPreference.edit()
        editor.putBoolean(TEST_KEY, true)
        editor.commit()

        assertTrue(persistentSettings.contains(TEST_KEY))
        assertFalse(persistentSettings.contains("SOMETHING"))
    }

    @Test
    fun remove() {
        val editor = sharedPreference.edit()
        editor.putBoolean(TEST_KEY, true)
        editor.commit()

        persistentSettings.remove(TEST_KEY)

        assertFalse(sharedPreference.contains(TEST_KEY))
    }

    @Test
    fun reset() {
        val editor = sharedPreference.edit()
        editor.putBoolean(TEST_KEY, true)
        editor.commit()

        persistentSettings.reset()

        assertFalse(sharedPreference.contains(TEST_KEY))
    }


    private fun classForTestJson(): String {
        val testObject = ClassForTest()
        val gson = Gson()
        return gson.toJson(testObject)
    }

    class ClassForTest {
        val property = "HELLO"
        val innerClassForTest = InnerClassForTest()
    }

    class InnerClassForTest {
        val property = 123
    }
}
