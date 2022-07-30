package com.paypay.currencyconverter.repository.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRefreshChecker @Inject constructor() {

    fun checkIfTimePassed(rateLmt: Long): Boolean {
        return System.currentTimeMillis() >= rateLmt + 30 * 60 * 1000
    }
}