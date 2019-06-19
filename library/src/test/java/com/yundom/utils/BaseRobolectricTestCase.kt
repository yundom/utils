package com.yundom.utils

import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [19, 27])
abstract class BaseRobolectricTestCase {
    @Rule
    @JvmField
    val injectMockRule = TestRule { statement, _ ->
        MockitoAnnotations.initMocks(this@BaseRobolectricTestCase)
        statement
    }

    val context = RuntimeEnvironment.application
}
