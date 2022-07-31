package com.paypay.currencyconverter.repository.remote

import com.paypay.currencyconverter.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val service: CurrencyService
) {
    suspend fun getCurrencyRate() = service.getCurrencyRate(BuildConfig.API_KEY)
}