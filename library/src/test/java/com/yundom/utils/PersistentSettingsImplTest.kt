package com.yundom.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PersistentSettingsImplTest {
    companion object {
        const val TEST_KEY = "TEST_KEY"
    }

    @Mock
    internal lateinit var sharedPreference: SharedPreferences

    @Mock
    internal lateinit var editor: SharedPreferences.Editor

    private lateinit var persistentSettings: PersistentSettings

    @Before
    fun setUp() {
        whenever(sharedPreference.edit()).thenReturn(editor)
        persistentSettings = PersistentSettingsImpl(sharedPreference)
    }

    @Test
    fun setBoolean() {
        persistentSettings.setBoolean(TEST_KEY, true)

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).putBoolean(TEST_KEY, true)
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun getBoolean() {
        whenever(sharedPreference.getBoolean(eq(TEST_KEY), any())).thenReturn(true)

        val result = persistentSettings.getBoolean(TEST_KEY, false)

        assertTrue(result)
    }

    @Test
    fun setInt() {
        persistentSettings.setInt(TEST_KEY, 123)

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).putInt(TEST_KEY, 123)
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun getInt() {
        whenever(sharedPreference.getInt(eq(TEST_KEY), any())).thenReturn(123)

        val result = persistentSettings.getInt(TEST_KEY, 0)

        assertEquals(123, result)
    }

    @Test
    fun setFloat() {
        persistentSettings.setFloat(TEST_KEY, 0.123f)

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).putFloat(TEST_KEY, 0.123f)
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun getFloat() {
        whenever(sharedPreference.getFloat(eq(TEST_KEY), any())).thenReturn(0.123f)

        val result = persistentSettings.getFloat(TEST_KEY, 0f)

        assertEquals(0.123f, result)
    }

    @Test
    fun setString() {
        persistentSettings.setString(TEST_KEY, "HELLO")

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).putString(TEST_KEY, "HELLO")
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun getString() {
        whenever(sharedPreference.getString(eq(TEST_KEY), any())).thenReturn("HELLO")

        val result = persistentSettings.getString(TEST_KEY, "HELLO")

        assertEquals("HELLO", result)
    }

    @Test
    fun setObject() {
        val testObject = ClassForTest()
        val gson = Gson()
        val serializedTestOBject = gson.toJson(testObject)
        persistentSettings.setObject(TEST_KEY, testObject)

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).putString(TEST_KEY, serializedTestOBject)
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun getObject() {
        val testObject = ClassForTest()
        val gson = Gson()
        val serializedTestOBject = gson.toJson(testObject)
        whenever(sharedPreference.getString(eq(TEST_KEY), anyOrNull())).thenReturn(serializedTestOBject)

        val result = persistentSettings.getObject(TEST_KEY, ClassForTest::class.java, null)

        assertTrue(result is ClassForTest)
        val obj = result as ClassForTest
        assertEquals(obj.property, testObject.property)
        assertEquals(obj.innerClassForTest.property, testObject.innerClassForTest.property)
    }

    @Test
    fun contains() {
        whenever(sharedPreference.contains(eq(TEST_KEY))).thenReturn(true)

        assertTrue(sharedPreference.contains(TEST_KEY))
        assertFalse(sharedPreference.contains("SOMETHING"))
    }

    @Test
    fun remove() {
        persistentSettings.remove(TEST_KEY)

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).remove(TEST_KEY)
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    @Test
    fun reset() {
        persistentSettings.reset()

        verify(sharedPreference, times(1)).edit()
        verify(editor, times(1)).clear()
        verify(editor, times(1)).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPreference)
    }

    class ClassForTest {
        val property = "HELLO"
        val innerClassForTest = InnerClassForTest()
    }

    class InnerClassForTest {
        val property = 123
    }
}
