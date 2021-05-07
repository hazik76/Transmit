package ru.planirui.transmit.utilits

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import ru.planirui.transmit.models.CommonModel

class DiffUtilCallback(
    private val oldList: List<CommonModel>,
    private val newList: List<CommonModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].timeStamp == newList[newItemPosition].timeStamp

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}