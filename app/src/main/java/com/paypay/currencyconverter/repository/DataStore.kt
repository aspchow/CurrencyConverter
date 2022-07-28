package com.paypay.currencyconverter.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStore @Inject constructor(@ApplicationContext val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "currency_rates")

    private val RATE_KEY = stringPreferencesKey("RATE_KEY")

    private val rateStateFlow = MutableStateFlow(Rate())

    suspend fun saveRate(rate: Rate) {
        rateStateFlow.value = rate
    }

    fun getRate(): Flow<Rate> = rateStateFlow

    private suspend fun <T> saveValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }
}