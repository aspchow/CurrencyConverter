package com.paypay.currencyconverter.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.paypay.currencyconverter.ui.theme.primaryColor

@Composable
fun NetworkLoader() {
    NetworkLoaderView()
}

@Preview
@Composable
private fun NetworkLoaderView() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Please wait while we are getting things ready")
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = primaryColor,
                strokeWidth = 2.dp
            )
        }
    }
}