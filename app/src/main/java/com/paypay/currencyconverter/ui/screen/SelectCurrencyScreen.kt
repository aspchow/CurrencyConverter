package com.paypay.currencyconverter.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paypay.currencyconverter.viewmodel.CurrencyConverterViewModel
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.repository.model.Currency
import com.paypay.currencyconverter.ui.theme.*

@Composable
fun SelectCurrency(navController: NavController, viewModel: CurrencyConverterViewModel) {


    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    SelectCurrencyView(selectedCurrency = selectedCurrency,
        onRequestBack = {
            navController.popBackStack()
        },
        onCurrencySelected = { currency ->
            viewModel.setTheCurrency(currency = currency)
            navController.popBackStack()
        })
}


@Preview
@Composable
fun SelectCurrencyViewPreview() {
    SelectCurrencyView(selectedCurrency = Currency.INR,
        onRequestBack = {

        },
        onCurrencySelected = {

        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectCurrencyView(
    selectedCurrency: Currency,
    onRequestBack: () -> Unit,
    onCurrencySelected: (Currency) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), elevation = 10.dp, shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(secondaryColor)
                .padding(top = 20.dp, start = 8.dp, end = 8.dp)

        ) {
            HeaderView(onRequestBack)

            Spacer(modifier = Modifier.height(16.dp))


            var searchQuery by remember {
                mutableStateOf("")
            }

            SearchBox(searchText = searchQuery, onSearch = { query ->
                searchQuery = query
            })

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cells = GridCells.Fixed(3),
                content = {
                    items(Currency.values().filter { currency ->
                        currency != selectedCurrency && if (searchQuery.isEmpty()) true else currency.name.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }) { currency ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCurrencySelected(currency)
                                }
                                .padding(5.dp),
                            elevation = 10.dp,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = currency.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                style = fontRegular.copy(
                                    color = textColor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Center
                                )
                            )

                        }
                    }
                },
                horizontalArrangement = Arrangement.Center,
            )


        }
    }
}

@Composable
private fun HeaderView(onRequestBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier.clickable {
                onRequestBack()
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                modifier = Modifier.size(height = 12.dp, width = 12.dp),
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Converter",
                style = fontRegular.copy(color = primaryColor, fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Change Currency",
            style = fontBold.copy(color = textColor, fontSize = 14.sp)
        )
    }
}