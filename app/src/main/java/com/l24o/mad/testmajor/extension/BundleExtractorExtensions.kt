package com.l24o.mad.testmajor.extension

import android.app.Activity
import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> Activity.extras(key: String? = null, defaultValue: T? = null): ReadWriteProperty<Activity, T> {
    return BundleExtractorDelegate { thisRef, property ->
        val bundleKey = key ?: property.name
        extractFromBundle(thisRef.intent?.extras, bundleKey, defaultValue)
    }
}

inline fun <reified T> extractFromBundle(bundle: Bundle?,
                                         key: String? = null,
                                         defaultValue: T? = null): T {

    val result = bundle?.get(key) ?: defaultValue

    if (result != null && result !is T) {
        throw ClassCastException("Property for $key has different class type")
    }

    return result as T
}

class BundleExtractorDelegate<R, T>(private val initializer: (R, KProperty<*>) -> T) : ReadWriteProperty<R, T> {

    private object EMPTY

    private var value: Any? = EMPTY

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }

        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}