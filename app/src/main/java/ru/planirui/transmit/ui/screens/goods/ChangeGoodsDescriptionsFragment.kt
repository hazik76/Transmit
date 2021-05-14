package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.setBioToDatabase
import ru.planirui.transmit.database.setGoodsDescriptionsToDatabase
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения описания товара */

class ChangeGoodsDescriptionsFragment(
    private val description: String,
    private val idGoods: String
) : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        initFields()
    }

    private fun initFields() {
        settings_input_bio.setText(description)
    }

    override fun change() {
        super.change()
        val newDescriptions = settings_input_bio.text.toString()
        setGoodsDescriptionsToDatabase(newDescriptions, idGoods)
    }
}