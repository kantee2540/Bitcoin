package com.example.bitcoin

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.bitcoin_recycler_item.view.*
import kotlinx.android.synthetic.main.bitcoin_recycler_item.view.recycler_icon
import kotlinx.android.synthetic.main.bitcoin_recycler_item.view.recycler_name
import kotlinx.android.synthetic.main.bitcoin_recycler_item2.view.*
import kotlinx.android.synthetic.main.processing_item.view.*

class BitcoinRecyclerAdapter(private var bitcoinItem: ArrayList<BitcoinModel>, private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var isSearching = false
    private var isLoadingAdded = false
    private var filteredItem: ArrayList<BitcoinModel> = ArrayList()

    companion object{
        const val ITEM = 0
        const val ITEM2 = 1
        const val ITEM_LOADING = 2
    }

    override fun getItemCount(): Int {
        return bitcoinItem.count()

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bitCoinItem = bitcoinItem[position]

        when {
            getItemViewType(position) == ITEM2 -> {
                val itemHolder2: ItemHolder2 = holder as ItemHolder2
                val iconURI = Uri.parse(bitCoinItem.iconURL)
                itemHolder2.recyclerName2.text = bitCoinItem.name
                GlideToVectorYou.init().with(context).load(iconURI, itemHolder2.recyclerIcon2)

            }

            getItemViewType(position) == ITEM_LOADING -> {
                val loadingHolder = holder as LoadingHolder
            }

            else -> {
                val itemHolder : ItemHolder = holder as ItemHolder
                val iconURI = Uri.parse(bitCoinItem.iconURL)
                itemHolder.recyclerName.text = bitCoinItem.name
                itemHolder.recyclerDescription.text = bitCoinItem.description
                GlideToVectorYou.init().with(context).load(iconURI, itemHolder.recyclerIcon)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM2 -> {
                ItemHolder2(LayoutInflater.from(context).inflate(R.layout.bitcoin_recycler_item2, parent, false))
            }
            ITEM_LOADING -> {
                LoadingHolder(LayoutInflater.from(context).inflate(R.layout.processing_item, parent, false))
            }
            else -> {
                ItemHolder(LayoutInflater.from(context).inflate(R.layout.bitcoin_recycler_item, parent, false))
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 5 == 4 && position != 0) {
            ITEM2
        } else if(isLoadingAdded){
            if(position == bitcoinItem.size - 1){
                ITEM_LOADING
            }else{
                ITEM
            }
        } else{
            ITEM
        }

    }

    fun addItem(item: ArrayList<BitcoinModel>){
        for(i in item){
            add(i)
        }
    }

    fun add(bitcoinModel: BitcoinModel){
        bitcoinItem.add(bitcoinModel)
        this.notifyDataSetChanged()
    }

    fun addFooter(){
        isLoadingAdded = true
        add(bitcoinModel = BitcoinModel())
    }

    fun removeFooter(){
        isLoadingAdded = false
        val position = bitcoinItem.size - 1
        bitcoinItem.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun searchItem(){

    }

    fun getItem(): ArrayList<BitcoinModel>{
        return bitcoinItem
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //For standard layout
        val recyclerName = itemView.recycler_name
        val recyclerDescription = itemView.recycler_description
        val recyclerIcon = itemView.recycler_icon

    }

    class ItemHolder2(itemView: View): RecyclerView.ViewHolder(itemView){
        //For right layout
        val recyclerName2 = itemView.recycler_name2
        val recyclerIcon2 = itemView.recycler_icon2
    }

    class LoadingHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val progressBar = itemView.progressBar
    }
}