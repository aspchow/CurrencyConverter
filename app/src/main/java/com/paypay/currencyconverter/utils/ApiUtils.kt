package com.paypay.currencyconverter.utils

import retrofit2.Response

fun <T> Response<T>.handle(onSuccess: (T) -> Unit, onFailure: (String) -> Unit) {
    if (isSuccessful) {
        onSuccess(body()!!)
    } else {
        onFailure(errorBody()!!.string())
    }
}