package com.example.bitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DownloadBitcoinInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadBitcoin = DownloadBitcoinItem(this)
        downloadBitcoin.downloadItem()



    }

    override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {
        runOnUiThread {
            val bitcoinRecyclerAdapter = BitcoinRecyclerAdapter(item, this)
            bitcoin_recycler.adapter = bitcoinRecyclerAdapter
            bitcoin_recycler.layoutManager = LinearLayoutManager(this)
        }

    }

    override fun downloadItemFailed(error: String) {

    }
}
