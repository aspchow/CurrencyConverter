package com.paypay.currencyconverter

import androidx.lifecycle.ViewModel
import com.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun getCurrencyRate() = repository.getCurrencyRate()
}