package com.example.bitcoin

interface DownloadBitcoinInterface {
    fun downloadItemSuccess(item: ArrayList<BitcoinModel>)
    fun downloadItemFailed(error: String)
}