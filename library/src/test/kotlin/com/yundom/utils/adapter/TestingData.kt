package com.yundom.utils.adapter

sealed class TestingData {
    data class TypeA(val data: String) : TestingData()
    data class TypeB(val data: String) : TestingData()
    data class TypeC(val data: String) : TestingData()
}
