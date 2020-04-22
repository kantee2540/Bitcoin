package com.example.bitcoin

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DownloadBitcoinItem(val callback: DownloadBitcoinInterface) {

    val url = "https://api.coinranking.com/v1/public/coins"

    companion object{
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val ICONURL = "iconUrl"
    }

    fun downloadItem(){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Download Failed", e.message.toString())
                callback.downloadItemFailed(e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val strResponse = response.body!!.string()
                val dataObject = JSONObject(strResponse).getJSONObject("data")
                val coinsObject = JSONArray(dataObject.getString("coins"))

                val bitCoinItem: ArrayList<BitcoinModel> = ArrayList()

                for(i in 0 until coinsObject.length()){
                    val json = coinsObject.getJSONObject(i)
                    val bitcoinModel = BitcoinModel()
                    bitcoinModel.name = json.getString(NAME)
                    bitcoinModel.description = json.getString(DESCRIPTION)
                    bitcoinModel.iconURL = json.getString(ICONURL)

                    bitCoinItem.add(bitcoinModel)

                }

                callback.downloadItemSuccess(bitCoinItem)

            }
        })
    }
}