package com.example.chucknorrisjokes

import org.junit.Assert.assertEquals

class Order {

    private val list = mutableListOf<String>()

    fun add(name: String) {
        list.add(name)
    }

    fun check(vararg expected: String) {
        assertEquals(expected.toList(), list)
    }
}