package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_my_goods.*
import ru.planirui.transmit.R
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.replaceFragment

/* Фрагмент вещей пользователя */

class MyGoodsFragment : BaseFragment(R.layout.fragment_my_goods) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_goods)
        initRecyclerView()
        initFields()
    }

    private fun initFields() {
        var idGoods = "-M_ex7V9CoMx9zmATqME"
        add_goods.setOnClickListener { replaceFragment(AddGoodsFragment("")) }
        add_goods2.setOnClickListener { replaceFragment(AddGoodsFragment(idGoods)) }
    }

    private fun initRecyclerView() {

    }
}