package com.paypay.currencyconverter.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paypay.currencyconverter.R
import com.paypay.currencyconverter.ui.theme.fontRegular
import com.paypay.currencyconverter.ui.theme.textColor

@Composable
fun SearchBox(searchText: String, onSearch: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(8.dp)
    ) {

        var isOnFocus by remember {
            mutableStateOf(false)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = R.drawable.search_currency),
                contentDescription = null
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .onFocusEvent {
                        isOnFocus = it.isFocused
                    },
                value = if (searchText.isEmpty() && isOnFocus.not()) "Search Currency" else searchText,
                onValueChange = onSearch,
                textStyle = fontRegular.copy(
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = if (searchText.isEmpty() && isOnFocus.not()) FontWeight.Normal else FontWeight.Bold
                )
            )
        }
    }
}