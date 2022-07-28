package com.paypay.currencyconverter

import androidx.lifecycle.ViewModel
import com.paypay.currencyconverter.repository.Rate
import com.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getCurrencyRate() = repository.getRate()

    fun getCurrencyRateFromServer() = repository.getCurrencyRateFromServer()
}