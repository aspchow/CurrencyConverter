package com.paypay.currencyconverter.repository.local

import com.paypay.currencyconverter.repository.model.Rate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val dataStore: DataStore) {

    fun getRate() = dataStore.getRate()

    suspend fun saveRate(rate: Rate) {
        dataStore.saveRate(rate = rate)
    }

    fun getLmtForRate() = dataStore.getLmtOFRate()
}