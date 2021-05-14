package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения описания товара */

class ChangeGoodsFragment(
    private val description: String,
    private val idGoods: String,
    private val changeName: String
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
        when(changeName){
            "description" -> setGoodsDescriptionsToDatabase(newDescriptions, idGoods)
            "extend" ->  setGoodsExtendToDatabase(newDescriptions, idGoods)
            "name" ->  setGoodsNameToDatabase(newDescriptions, idGoods)
        }
    }
}