package com.dc.module_main

import com.blankj.utilcode.util.LogUtils

val stringLenthFunc: (String) -> Int = { input ->
    input.length
}

fun main() {
    val str: Int = stringLenthFunc("Android")
    println("ldl"+str)
}