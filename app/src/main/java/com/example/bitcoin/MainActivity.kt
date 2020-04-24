package com.example.bitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private  var downloadedItem: ArrayList<BitcoinModel> = ArrayList()
    private lateinit var adapter: BitcoinRecyclerAdapter

    companion object {
        const val PAGE_START = 1
        const val TOTAL_PAGE = 10
    }

    private var isSearching = false
    private var firstSearchingType = false

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START

    private var offset = 0
    private var limit = 10
    private var keyword = ""

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
            currentPage = PAGE_START
            isLastPage = false
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
                loading_activity.visibility = View.VISIBLE
                if (p0.toString() != "") {
                    pull_to_refresh.isEnabled = false
                    isSearching = true
                    clear_text_button.visibility = View.VISIBLE

                    offset = 0
                    keyword = p0.toString()
                    loadSearchItem(keyword)


                } else {

                    isSearching = false
                    firstSearchingType = false

                    offset = 0
                    loadFirstPage()

                    pull_to_refresh.isEnabled = true
                    clear_text_button.visibility = View.GONE
                }
            }
        })
    }

    private fun loadFirstPage() {
        val downloadBitcoin = DownloadBitcoinItem(object : DownloadBitcoinInterface {
            override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {

                downloadedItem = item
                runOnUiThread {

                    setUpRecyclerView(item)
                    loading_activity.visibility = View.GONE
                    pull_to_refresh.isRefreshing = false

                    if (currentPage <= TOTAL_PAGE && !isSearching) adapter.addFooter()
                    else isLastPage = true
                }

            }

            override fun downloadItemFailed(error: String) {
                pull_to_refresh.isRefreshing = false
                loading_activity.visibility = View.GONE
            }
        })

        downloadBitcoin.downloadItem(offset = offset, limit = limit)

    }

    private fun loadNextPage(){
        offset += 10
        val downloadBitcoin = DownloadBitcoinItem(object : DownloadBitcoinInterface {
            override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {

                runOnUiThread {
                    adapter.removeFooter()
                    isLoading = false
                    adapter.addItem(item)
                    downloadedItem.addAll(item)

                    if (currentPage != TOTAL_PAGE) adapter.addFooter()
                    else isLastPage = true
                }

            }

            override fun downloadItemFailed(error: String) {
                adapter.removeFooter()
                pull_to_refresh.isRefreshing = false
                loading_activity.visibility = View.GONE
            }
        })

        downloadBitcoin.downloadItem(offset = offset, limit = limit)

    }

    private fun loadSearchItem(keyword: String){
        val downloadBitcoin = DownloadBitcoinItem(object : DownloadBitcoinInterface{
            override fun downloadItemSuccess(item: ArrayList<BitcoinModel>) {

                downloadedItem = item
                runOnUiThread {
                    setUpRecyclerView(item)
                    loading_activity.visibility = View.GONE

                    if (currentPage <= TOTAL_PAGE && isSearching) adapter.addFooter()
                    else isLastPage = true
                }
            }

            override fun downloadItemFailed(error: String) {
                loading_activity.visibility = View.GONE
            }
        })

        downloadBitcoin.searchItem(keyword)
    }

    private fun setUpRecyclerView(item: ArrayList<BitcoinModel>) {
        adapter = BitcoinRecyclerAdapter(item, this)
        val layoutManager = LinearLayoutManager(this)

        bitcoin_recycler.adapter = adapter
        bitcoin_recycler.layoutManager = layoutManager

        bitcoin_recycler.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                if(!isSearching) {
                    isLoading = true
                    currentPage += 1
                    loadNextPage()
                }

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

            override fun isSearching(): Boolean {
                return isSearching
            }
        })
    }
}
