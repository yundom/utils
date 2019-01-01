package com.yundom.utils.adapter

sealed class TestingData2 {
    data class TypeD(val data: String) : TestingData2()
    data class TypeE(val data: String) : TestingData2()
    data class TypeF(val data: String) : TestingData2()
}
