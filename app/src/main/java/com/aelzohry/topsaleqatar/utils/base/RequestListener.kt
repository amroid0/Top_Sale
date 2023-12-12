package com.aelzohry.topsaleqatar.utils.base

/**
 * # Created by Mousa Hashihso on 17/01/2023.
 */
public interface RequestListener<R> {
    fun onResult(result: R)
}