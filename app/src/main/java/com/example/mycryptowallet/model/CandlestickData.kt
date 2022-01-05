package com.example.mycryptowallet.model

data class CandlestickData(
        val openTime: String,
        val open: String,
        val high :String,
        val low: String,
        val close: String,
        val volume: String,
        val closeTime: String,
        val QuoteAssetVolume: String,
        val numberOfTrades: String,
        val takerBuyBaseAssetVolume: String,
        val takerBuyQuoteAssetVolume: String,
        val ignore: String) {
}
