package com.paypay.currencyconverter

import androidx.lifecycle.ViewModel
import com.paypay.currencyconverter.repository.Currency
import com.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {


    private val _selectedCurrency = MutableStateFlow(Currency.USD)
    val selectedCurrency: StateFlow<Currency> = _selectedCurrency

    private val _searchedCurrency = MutableStateFlow("")

    val searchedCurrency: StateFlow<String> = _searchedCurrency


    val availableCurrencies: Flow<List<Currency>> = _searchedCurrency.flatMapLatest { search ->
        _selectedCurrency.map { selectedCurrecy ->
            if (search.isEmpty()) {
                Currency.values().filter { currency ->
                    currency != selectedCurrecy
                }
            } else {
                Currency.values().filter { currency ->
                    currency.name.contains(search, ignoreCase = true) && currency != selectedCurrecy
                }
            }
        }
    }


    fun getCurrencyRate() = repository.getRate()

    fun getCurrencyRateFromServer() = repository.getCurrencyRateFromServer()

    fun setTheCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }

    fun search(query: String) {
        _searchedCurrency.value = query
    }

}