package com.example.bitcoin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bitcoin_recycler_item.view.*

class BitcoinRecyclerAdapter(val bitcoinItem: ArrayList<BitcoinModel>, val context: Context): RecyclerView.Adapter<BitcoinRecyclerAdapter.Holder>(){

    override fun getItemCount(): Int {
        return bitcoinItem.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bitCoinItem = bitcoinItem[position]
        holder.recyclerName.text = bitCoinItem.name
        holder.recyclerDescription.text = bitCoinItem.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.bitcoin_recycler_item, parent, false))
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recyclerName = itemView.recycler_name
        val recyclerDescription = itemView.recycler_description
        val recyclerIcon = itemView.recycler_icon
    }
}