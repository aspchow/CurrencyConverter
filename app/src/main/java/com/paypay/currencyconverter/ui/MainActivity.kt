package com.paypay.currencyconverter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.paypay.currencyconverter.viewmodel.CurrencyConverterViewModel
import com.paypay.currencyconverter.ui.screen.ConvertCurrencyScreen
import com.paypay.currencyconverter.ui.screen.Screen
import com.paypay.currencyconverter.ui.screen.SelectCurrency
import com.paypay.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {

                val navController = rememberNavController()
                val viewModel = hiltViewModel<CurrencyConverterViewModel>()


                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.CONVERT_CURRENCY,
                    ) {
                        composable(route = Screen.CONVERT_CURRENCY) {
                            ConvertCurrencyScreen(navController = navController, viewModel)
                        }

                        dialog(
                            route = Screen.SELECT_CURRENCY,
                            dialogProperties = DialogProperties(dismissOnClickOutside = false)
                        ) {
                            SelectCurrency(navController = navController, viewModel)
                        }

                    }


                }
            }
        }
    }
}