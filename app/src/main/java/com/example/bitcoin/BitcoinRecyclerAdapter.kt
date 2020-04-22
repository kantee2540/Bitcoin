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

class BitcoinRecyclerAdapter(private val bitcoinItem: ArrayList<BitcoinModel>, private val context: Context)
    : RecyclerView.Adapter<BitcoinRecyclerAdapter.Holder>(){

    override fun getItemCount(): Int {
        return bitcoinItem.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bitCoinItem = bitcoinItem[position]
        val iconURI = Uri.parse(bitCoinItem.iconURL)

        if(position % 5 == 4 && position != 0) {
            holder.recyclerName2.text = bitCoinItem.name
            GlideToVectorYou.init().with(context).load(iconURI, holder.recyclerIcon2)

        }else{
            holder.recyclerName.text = bitCoinItem.name
            holder.recyclerDescription.text = bitCoinItem.description
            GlideToVectorYou.init().with(context).load(iconURI, holder.recyclerIcon)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType == 1){
            Holder(LayoutInflater.from(context).inflate(R.layout.bitcoin_recycler_item2, parent, false))
        }else{
            Holder(LayoutInflater.from(context).inflate(R.layout.bitcoin_recycler_item, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 5 == 4 && position != 0) {
            1
        }
        else{
            super.getItemViewType(position)
        }

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //For standard layout
        val recyclerName = itemView.recycler_name
        val recyclerDescription = itemView.recycler_description
        val recyclerIcon = itemView.recycler_icon

        //For right layout
        val recyclerName2 = itemView.recycler_name2
        val recyclerIcon2 = itemView.recycler_icon2
    }
}