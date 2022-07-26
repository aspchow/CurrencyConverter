package com.paypay.currencyconverter.repository

import com.paypay.currencyconverter.utils.handle
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    fun getCurrencyRate() = flow<Result<CurrencyRate>> {
        val result = remoteDataSource.getCurrencyRate()
        result.handle(
            onSuccess = {
            },
            onFailure = {

            }
        )
    }
}
