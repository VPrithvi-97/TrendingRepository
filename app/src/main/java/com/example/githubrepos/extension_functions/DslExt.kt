package com.example.githubrepos.extension_functions

import android.location.Location
import android.view.View

operator fun <T> T.invoke(block: T.() -> Unit) = block()


inline fun <reified T : View?> views(vararg elements: T?): Array<T?> {
    return arrayOf(*elements)
}

operator fun Location.component1() = latitude
operator fun Location.component2() = longitude