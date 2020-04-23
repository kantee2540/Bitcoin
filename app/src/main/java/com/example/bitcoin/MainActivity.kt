package com.example.bitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.notify
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private  var downloadedItem: ArrayList<BitcoinModel> = ArrayList()
    lateinit var adapter: BitcoinRecyclerAdapter

    companion object {
        const val PAGE_START = 1
        const val TOTAL_PAGE = 10
    }

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START
    private var offset = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dividerItemDecoration =
            DividerItemDecoration(bitcoin_recycler.context, DividerItemDecoration.VERTICAL)
        bitcoin_recycler.addItemDecoration(dividerItemDecoration)

        loadFirstPage()
        searchItem()

        pull_to_refresh.setOnRefreshListener {
            offset = 0
            loadFirstPage()
        }
    }

    private fun searchItem() {
        clear_text_button.visibility = View.GONE
        clear_text_button.setOnClickListener {
            search_editText.setText("")
        }


        search_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredItem: ArrayList<BitcoinModel> = ArrayList()
                if (p0.toString() != "") {
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

                } else {
                    clear_text_button.visibility = View.GONE
                    setUpRecyclerView(downloadedItem)
                }
            }
        })
    }

    private fun loadFirstPage() {
        val downloadBitcoin = DownloadBitcoinItem(object : DownloadBitcoinInterface {
            override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {
                runOnUiThread {
                    downloadedItem = item
                    setUpRecyclerView(downloadedItem)
                    loading_activity.visibility = View.GONE
                    pull_to_refresh.isRefreshing = false
                }

            }

            override fun downloadItemFailed(error: String) {
                pull_to_refresh.isRefreshing = false
                loading_activity.visibility = View.GONE
            }
        })
        downloadBitcoin.downloadItem(offset = offset)
    }

    private fun loadNextPage(){
        offset += 10
        val downloadBitcoin = DownloadBitcoinItem(object : DownloadBitcoinInterface {
            override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {
                runOnUiThread {
                    isLoading = false
                    adapter.addItem(item)
                    loading_activity.visibility = View.GONE
                }

            }

            override fun downloadItemFailed(error: String) {
                pull_to_refresh.isRefreshing = false
                loading_activity.visibility = View.GONE
            }
        })
        downloadBitcoin.downloadItem(offset = offset)
    }

    private fun setUpRecyclerView(item: ArrayList<BitcoinModel>) {
        adapter = BitcoinRecyclerAdapter(item, this)
        val layoutManager = LinearLayoutManager(this)

        bitcoin_recycler.adapter = adapter
        bitcoin_recycler.layoutManager = layoutManager

        bitcoin_recycler.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                loading_activity.visibility = View.VISIBLE
                isLoading = true
                currentPage += 1
                loadNextPage()
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGE
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }
}
