package com.example.bitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DownloadBitcoinInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloadBitcoinItem()

        pull_to_refresh.setOnRefreshListener {
            downloadBitcoinItem()
        }

    }

    private fun downloadBitcoinItem() {
        val downloadBitcoin = DownloadBitcoinItem(this)
        downloadBitcoin.downloadItem()
    }

    override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {
        runOnUiThread {
            val bitcoinRecyclerAdapter = BitcoinRecyclerAdapter(item, this)
            val dividerItemDecoration = DividerItemDecoration(bitcoin_recycler.context, DividerItemDecoration.VERTICAL)

            bitcoin_recycler.adapter = bitcoinRecyclerAdapter
            bitcoin_recycler.layoutManager = LinearLayoutManager(this)
            bitcoin_recycler.addItemDecoration(dividerItemDecoration)

            pull_to_refresh.isRefreshing = false
            loading_activity.visibility = View.GONE
        }

    }

    override fun downloadItemFailed(error: String) {
        runOnUiThread {
            Toast.makeText(this, R.string.error_mes, Toast.LENGTH_LONG).show()
            pull_to_refresh.isRefreshing = false
            loading_activity.visibility = View.GONE
        }
    }
}
