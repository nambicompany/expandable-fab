package com.nambimobile.widgets.efab

@JvmSynthetic
internal fun illegalState(message: String, cause: Throwable? = null): Nothing =
    throw IllegalStateException(message, cause)

@JvmSynthetic
internal fun illegalArg(message: String, cause: Throwable? = null): Nothing =
    throw IllegalArgumentException(message, cause)