// Copyright 2017 Canva Inc. All Rights Reserved.

package com.romanzes.tinyreddit.util

sealed class Optional<out T> {
    private data class Present<T>(override val value: T) : Optional<T>() {
        override val isPresent: Boolean
            get() = true

        override fun getOrThrow(): T = value
    }

    private object Absent : Optional<Nothing>() {
        override val isPresent: Boolean
            get() = false

        override fun getOrThrow(): Nothing = throw NullPointerException()

        override val value: Nothing? = null
    }

    abstract val isPresent: Boolean

    /**
     * @return the value or throw if absent
     * @throws NullPointerException if the value is absent
     */
    abstract fun getOrThrow(): T

    /**
     * @return the nullable value of this Optional
     */
    abstract val value: T?

    companion object {
        fun <T> of(value: T?): Optional<T> = value?.let { Present(it) } ?: absent()

        @Suppress("UNCHECKED_CAST")
        fun <T> absent() = Absent as Optional<T>
    }
}

fun <T : Any> T?.asOptional(): Optional<T> =
    if (this == null) Optional.absent() else Optional.of(this)
