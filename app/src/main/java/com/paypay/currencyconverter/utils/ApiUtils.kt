package com.paypay.currencyconverter.utils

import retrofit2.Response

suspend fun <T> Response<T>.handle(onSuccess: suspend (T) -> Unit, onFailure: suspend (String) -> Unit) {
    if (isSuccessful) {
        onSuccess(body()!!)
    } else {
        onFailure(errorBody()!!.string())
    }
}