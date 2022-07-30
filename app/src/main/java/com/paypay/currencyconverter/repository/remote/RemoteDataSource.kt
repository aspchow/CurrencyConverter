package com.paypay.currencyconverter.repository.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val service: CurrencyService
) {
    suspend fun getCurrencyRate() = service.getCurrencyRate("d174b30ba60740d4a5beb8c09de1f701")
}