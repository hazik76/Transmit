package ru.planirui.transmit.ui.screens.goods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.planirui.transmit.R
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.utilits.downloadAndSetImageGoods
import ru.planirui.transmit.utilits.replaceFragment

class MyGoodsAdapter : RecyclerView.Adapter<MyGoodsAdapter.GoodsListHolder>() {

    private var listItems = mutableListOf<CommonModel>()

    class GoodsListHolder(view: View) : RecyclerView.ViewHolder(view) {
        var goodsName: TextView? = null
        var goodsStatus: TextView? = null
        var goodsPhoto: ImageView? = null

        init {
            goodsName = view.findViewById(R.id.goods_list_item_name)
            goodsStatus = view.findViewById(R.id.goods_list_status)
            goodsPhoto = view.findViewById(R.id.goods_list_item_photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.goods_list_item, parent, false)
        val holder = GoodsListHolder(view)
        holder.itemView.setOnClickListener {
            replaceFragment(AddGoodsFragment(listItems[holder.adapterPosition].goodsID))
        }
        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: GoodsListHolder, position: Int) {
        holder.goodsName?.text = listItems[position].name
        holder.goodsStatus?.text = listItems[position].status
        if (listItems[position].uriPhoto != "") {
            holder.goodsPhoto?.downloadAndSetImageGoods(listItems[position].uriPhoto)
        }
    }

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}