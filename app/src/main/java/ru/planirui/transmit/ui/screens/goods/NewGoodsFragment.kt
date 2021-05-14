package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения описания товара, но с добавлением всех недостающих полей */

class NewGoodsFragment(
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
        when (changeName) {
            "description" -> {
                newGoodsCreate() { idGoods ->
                    setGoodsDescriptionsToDatabase(newDescriptions, idGoods)
                }
            }
            "extend" -> {
                newGoodsCreate() { idGoods ->
                    setGoodsExtendToDatabase(newDescriptions, idGoods)
                }
            }
            "name" -> {
                newGoodsCreate() { idGoods ->
                    setGoodsNameToDatabase(newDescriptions, idGoods)
                }
            }
        }
    }
}