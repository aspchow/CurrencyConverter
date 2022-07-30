package com.paypay.currencyconverter.ui.screen

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paypay.currencyconverter.viewmodel.CurrencyConverterViewModel
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.repository.model.Currency
import com.paypay.currencyconverter.repository.model.Rate
import com.paypay.currencyconverter.ui.theme.*
import com.paypay.currencyconverter.utils.roundOff
import com.paypay.currencyconverter.utils.safeDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext


@Composable
fun ConvertCurrencyScreen(
    navController: NavController,
    viewModel: CurrencyConverterViewModel
) {


    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getCurrencyRateFromServer().collect { result ->

        }
    })

    val rate: Rate by viewModel.getCurrencyRate().collectAsState(initial = Rate())
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val availableCurrencies by viewModel.availableCurrencies.collectAsState(initial = emptyList())
    val searchedCurrency by viewModel.searchedCurrency.collectAsState()

    var price by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {

        CurrencyConverterBackGround()

        CurrencyConverterView(
            selectedCurrency = selectedCurrency,
            priceValue = price,
            onPriceChange = {
                price = it
            },
            currencies = availableCurrencies,
            rate = rate,
            searchText = searchedCurrency,
            onSearch = { query ->
                viewModel.search(query = query)
            }
        ) {
            navController.navigate(Screen.SELECT_CURRENCY)
        }

    }
}


@Preview
@Composable
fun CurrencyConverterPreview() {

    Box(modifier = Modifier.fillMaxSize()) {

        CurrencyConverterBackGround()

        CurrencyConverterView(
            Currency.INR, "",
            {

            },
            currencies = Currency.values().asList(),
            Rate(),
            "",
            {

            }
        ) {

        }

    }

}


@Composable
private fun CurrencyConverterView(
    selectedCurrency: Currency,
    priceValue: String,
    onPriceChange: (String) -> Unit,
    currencies: List<Currency>,
    rate: Rate,
    searchText: String,
    onSearch: (String) -> Unit,
    onCurrencyChangeRequest: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))

        SelectedCurrency(
            selectedCurrency = selectedCurrency,
            priceValue = priceValue,
            onPriceChange = onPriceChange,
            onCurrencyChangeRequest = onCurrencyChangeRequest
        )

        Spacer(modifier = Modifier.height(24.dp))

        CurrencyConversionSection(
            currencies = currencies,
            rate = rate,
            selectedCurrency = selectedCurrency,
            price = priceValue,
            searchText = searchText,
            onSearch = onSearch
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyConversionSection(
    currencies: List<Currency>,
    rate: Rate,
    selectedCurrency: Currency,
    price: String,
    searchText: String,
    onSearch: (String) -> Unit
) {

    Column(
        Modifier.fillMaxWidth()
    ) {

        SearchBox(searchText = searchText, onSearch = onSearch)

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            cells = GridCells.Fixed(2),
            content = {
                items(currencies) { currency ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = 10.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            val text = buildAnnotatedString {
                                append(currency.name)

                                Text(
                                    text = currency.name,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = fontRegular.copy(
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.W600,
                                        textAlign = TextAlign.Center
                                    )
                                )


                                Text(
                                    text = " (rate :- 1 ${selectedCurrency.name} = ${
                                        (rate.getRateFor(currency) / rate.getRateFor(
                                            selectedCurrency
                                        )).roundOff()
                                    } ${currency.name} )",
                                    style = fontRegular.copy(
                                        color = textColor,
                                        fontSize = 10.sp,
                                    )
                                )

                                Text(
                                    text = ((rate.getRateFor(currency) / rate.getRateFor(
                                        selectedCurrency
                                    )) * price.safeDouble()).roundOff().toString(),
                                    style = fontRegular.copy(
                                        color = textColor,
                                        fontSize = 13.sp,
                                    )
                                )
                            }
                        }
                    }
                }
            },
            horizontalArrangement = Arrangement.Center,
        )

    }
}

@Composable
private fun SelectedCurrency(
    selectedCurrency: Currency,
    priceValue: String,
    onPriceChange: (String) -> Unit,
    onCurrencyChangeRequest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedCurrency.name,
                    style = fontRegular.copy(fontSize = 16.sp, fontWeight = FontWeight.W600),
                    color = textColor
                )

                Spacer(modifier = Modifier.width(24.dp))

                var isTextFocused by remember {
                    mutableStateOf(false)
                }

                BasicTextField(
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (isTextFocused) primaryColor else secondaryColor,
                            RoundedCornerShape(16.dp)
                        )
                        .fillMaxWidth()
                        .padding(start = 14.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .onFocusEvent { it ->
                            isTextFocused = it.isFocused
                        },
                    value = priceValue.toString(),
                    onValueChange = { price ->
                        try {
                            if (price.isEmpty()) {
                                onPriceChange("")
                            } else {
                                price.toFloat()
                                onPriceChange(price)
                            }
                        } catch (_: Exception) {

                        }
                    },
                    textStyle = fontRegular.copy(
                        fontSize = 16.sp, fontWeight = FontWeight.W600, color = textColor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCurrencyChangeRequest()
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.change_currency_icon),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    "Change Currency",
                    style = fontBold.copy(color = primaryColor, fontSize = 17.sp),
                    color = primaryColor
                )
            }


        }
    }
}

@Preview
@Composable
private fun HeaderSection() {
    Spacer(modifier = Modifier.height(32.dp))
    Text(
        modifier = Modifier.padding(start = 20.dp),
        text = "Currency Converter",
        color = Color.White,
        style = fontBold.copy(fontSize = 20.sp)
    )
}

@Preview
@Composable
fun CurrencyConverterBackGround() {
    Box(Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(color = secondaryColor)
        )

        Spacer(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 0,
                        topEndPercent = 0,
                        bottomEndPercent = 30,
                        bottomStartPercent = 30
                    )
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryColor, primaryColor.copy(alpha = 0.5f)
                        )
                    )
                )
        )
    }
}
