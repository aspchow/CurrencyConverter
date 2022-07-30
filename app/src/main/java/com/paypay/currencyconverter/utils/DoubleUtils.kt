package com.paypay.currencyconverter.utils

fun String.safeDouble(): Double = try {
    toDouble()
} catch (_: Exception) {
    0.0
}

fun Double.roundOff(noOfDigits: Int = 3): Double = "%.${noOfDigits}f".format(this).safeDouble()


