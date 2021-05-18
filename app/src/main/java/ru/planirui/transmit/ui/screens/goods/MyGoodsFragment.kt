package ru.planirui.transmit.ui.screens.goods

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_my_goods.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.AppValueEventListener
import ru.planirui.transmit.utilits.replaceFragment

/* Фрагмент вещей пользователя */

class MyGoodsFragment : BaseFragment(R.layout.fragment_my_goods) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MyGoodsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_goods)
        initRecyclerView()
        initFields()
    }

    private fun initFields() {
        add_goods.setOnClickListener { replaceFragment(AddGoodsFragment("")) }
    }

    private fun initRecyclerView() {
        mRecyclerView = my_goods_recycle_view
        mAdapter = MyGoodsAdapter()
        getGoodsListInfo { mListItems ->
            mListItems.forEach { goods ->
                mAdapter.updateListItems(goods)
            }
            settings_label_goods_sum.setText(mListItems.size.toString())
        }
        mRecyclerView.adapter = mAdapter
    }
}