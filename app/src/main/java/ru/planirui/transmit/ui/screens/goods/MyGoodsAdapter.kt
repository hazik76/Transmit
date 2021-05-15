package ru.planirui.transmit.ui.screens.goods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.goods_list_item.view.*
import ru.planirui.transmit.R
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.utilits.*

class MyGoodsAdapter : RecyclerView.Adapter<MyGoodsAdapter.GoodsListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class GoodsListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goodsName: TextView = view.goods_list_item_name
        val goodsStatus: TextView = view.goods_list_status
        val goodsPhoto: ImageView = view.goods_list_item_photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.goods_list_item, parent,false)
        val holder = GoodsListHolder(view)
        holder.itemView.setOnClickListener {
            replaceFragment(AddGoodsFragment(listItems[holder.adapterPosition].goodsID))
        }
        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: GoodsListHolder, position: Int) {
        holder.goodsName.text = listItems[position].name
        holder.goodsStatus.text = listItems[position].status
        if (listItems[position].uriPhoto !=""){
            holder.goodsPhoto.downloadAndSetImageGoods(listItems[position].uriPhoto)
        }
//        when (listItems[position].goodsStatus){
//            GOODS_STATUS_ADDED -> ( println("status added"))
//            GOODS_STATUS_PLAYS -> ( println("status plays"))
//            GOODS_STATUS_NO_RECEIVED -> ( println("status no receiver"))
//            GOODS_STATUS_RECEIVED -> ( println("status received"))
//        }
    }

    fun updateListItems(item:CommonModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
        println(listItems.size)
    }
}