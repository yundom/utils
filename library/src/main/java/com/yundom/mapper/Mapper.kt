package com.yundom.mapper

interface Mapper<I, O> {
    fun map(value: I): O
}
