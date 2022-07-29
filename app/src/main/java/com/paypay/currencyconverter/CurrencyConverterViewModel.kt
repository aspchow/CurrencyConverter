package com.paypay.currencyconverter

import androidx.lifecycle.ViewModel
import com.paypay.currencyconverter.repository.Currency
import com.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {


    private val _selectedCurrency = MutableStateFlow(Currency.USD)

    val selectedCurrency : Flow<Currency> = _selectedCurrency

    fun getCurrencyRate() = repository.getRate()

    fun getCurrencyRateFromServer() = repository.getCurrencyRateFromServer()

    fun setTheCurrency(currency: Currency){
        _selectedCurrency.value = currency
    }

}