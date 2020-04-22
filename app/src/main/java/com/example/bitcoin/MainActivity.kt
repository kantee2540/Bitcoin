package com.example.bitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), DownloadBitcoinInterface {

    var downloadedItem :ArrayList<BitcoinModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dividerItemDecoration = DividerItemDecoration(bitcoin_recycler.context, DividerItemDecoration.VERTICAL)
        bitcoin_recycler.addItemDecoration(dividerItemDecoration)

        downloadBitcoinItem()
        searchItem()

        pull_to_refresh.setOnRefreshListener {
            downloadBitcoinItem()
        }
    }

    private fun searchItem(){
        clear_text_button.visibility = View.GONE
        clear_text_button.setOnClickListener {
            search_editText.setText("")
        }


        search_editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredItem: ArrayList<BitcoinModel> = ArrayList()
                if(p0.toString() != "") {
                    clear_text_button.visibility = View.VISIBLE
                    for (item in downloadedItem) {
                        when {
                            item.name.toLowerCase().startsWith(p0.toString().toLowerCase())
                            -> {
                                filteredItem.add(item)
                            }
                            item.slug.toLowerCase().startsWith(p0.toString().toLowerCase()) -> {
                                filteredItem.add(item)
                            }
                            item.symbols.toLowerCase().startsWith(p0.toString().toLowerCase()) -> {
                                filteredItem.add(item)
                            }
                        }

                        setUpRecyclerView(filteredItem)
                    }

                }

                else{
                    clear_text_button.visibility = View.GONE
                    setUpRecyclerView(downloadedItem)
                }
            }
        })
    }

    private fun downloadBitcoinItem() {
        val downloadBitcoin = DownloadBitcoinItem(this)
        downloadBitcoin.downloadItem()
    }

    override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {
        runOnUiThread {
            downloadedItem = item
            setUpRecyclerView(downloadedItem)
            pull_to_refresh.isRefreshing = false
            loading_activity.visibility = View.GONE
        }

    }

    private fun setUpRecyclerView(item: ArrayList<BitcoinModel>){
        val bitcoinRecyclerAdapter = BitcoinRecyclerAdapter(item, this)

        bitcoin_recycler.adapter = bitcoinRecyclerAdapter
        bitcoin_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun downloadItemFailed(error: String) {
        runOnUiThread {
            Toast.makeText(this, R.string.error_mes, Toast.LENGTH_LONG).show()
            pull_to_refresh.isRefreshing = false
            loading_activity.visibility = View.GONE
        }
    }
}
