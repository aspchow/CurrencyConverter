package com.paypay.currencyconverter.repository

import com.paypay.currencyconverter.utils.handle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun getRate() = localDataSource.getRate().flowOn(Dispatchers.IO)

    fun getCurrencyRateFromServer() = flow<Result<Unit>> {
        val result = remoteDataSource.getCurrencyRate()
        result.handle(
            onSuccess = {
                localDataSource.saveRate(it.rates)
                emit(Result.success(Unit))
            },
            onFailure = {
                emit(Result.failure(Exception("something went wrong please try again")))
            }
        )
    }
}
