package com.yundom.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 27])
abstract class BaseRobolectricTestCase {
    @Rule
    @JvmField
    val injectMockRule = TestRule { statement, _ ->
        MockitoAnnotations.initMocks(this@BaseRobolectricTestCase)
        statement
    }

    val context: Context = ApplicationProvider.getApplicationContext()
}
