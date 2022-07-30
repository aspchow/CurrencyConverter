package com.paypay.currencyconverter.repository

import com.paypay.currencyconverter.repository.remote.ApiRefreshChecker
import com.paypay.currencyconverter.repository.local.LocalDataSource
import com.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.paypay.currencyconverter.utils.handle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val apiRefreshChecker: ApiRefreshChecker
) {

    fun getRate() = localDataSource.getRate()

    fun getCurrencyRateFromServer() = flow<Result<Unit>> {
        if (apiRefreshChecker.checkIfTimePassed(localDataSource.getLmtForRate().first()).not()) {
            emit(Result.success(Unit))
            return@flow
        }
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
    }.catch {
        emit(Result.failure(Exception("something went wrong please try again")))
    }
}
