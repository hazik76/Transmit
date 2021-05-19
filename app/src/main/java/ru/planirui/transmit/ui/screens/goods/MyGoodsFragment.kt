package ru.planirui.transmit.ui.screens.goods

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.planirui.transmit.R
import ru.planirui.transmit.database.getGoodsListInfo
import ru.planirui.transmit.databinding.FragmentMyGoodsBinding
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.replaceFragment

/* Фрагмент вещей пользователя */

class MyGoodsFragment : BaseFragment(R.layout.fragment_my_goods) {

    private var binding: FragmentMyGoodsBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MyGoodsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_goods)
        initRecyclerView()
        initFields()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyGoodsBinding.bind(view)
    }

    private fun initFields() {
        binding?.addGoods?.setOnClickListener { replaceFragment(AddGoodsFragment("")) }
    }

    private fun initRecyclerView() {
        mRecyclerView = binding?.myGoodsRecycleView!! //my_goods_recycle_view
        mAdapter = MyGoodsAdapter()
        getGoodsListInfo { mListItems ->
            mListItems.forEach { goods ->
                mAdapter.updateListItems(goods)
            }
            binding?.settingsLabelGoodsSum?.text = mListItems.size.toString()
        }
        mRecyclerView.adapter = mAdapter
    }
}