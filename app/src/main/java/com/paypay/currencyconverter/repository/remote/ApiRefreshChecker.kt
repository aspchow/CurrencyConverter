package com.paypay.currencyconverter.repository.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRefreshChecker @Inject constructor() {

    companion object {
        const val API_CACHE_MILLISECONDS = 30 * 60 * 1000
    }

    fun checkIfTimePassed(rateLmt: Long): Boolean {
        return System.currentTimeMillis() >= rateLmt + API_CACHE_MILLISECONDS
    }
}