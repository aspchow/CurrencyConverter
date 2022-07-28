package com.paypay.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.paypay.currencyconverter.repository.Currency
import com.paypay.currencyconverter.repository.CurrencyRate
import com.paypay.currencyconverter.repository.Rate
import com.paypay.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<CurrencyConverterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            LaunchedEffect(key1 = Unit, block = {
                viewModel.getCurrencyRateFromServer().collect { result ->

                }
            })

            val rate: Rate by viewModel.getCurrencyRate().collectAsState(initial = Rate())

            CurrencyConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LazyColumn(content = {
                        items(Currency.values()) { currency ->
                            Text(text = currency.name + " " + rate.getRateFor(currency))
                        }
                    })
                }
            }
        }
    }
}