package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY

/* Фрагмент для изменения описания товара, но с добавлением всех недостающих полей */

class NewGoodsFragment(
    private val description: String,
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
        newGoodsCreate() { idGoods ->
            USER.tempMessage = idGoods
            when (changeName) {
                "name" -> setGoodsNameToDatabase(newDescriptions, idGoods)
                "description" -> setGoodsDescriptionsToDatabase(newDescriptions, idGoods)
                "extend" -> setGoodsExtendToDatabase(newDescriptions, idGoods)
                else -> {
                    println("что-то пошло не так")
                    APP_ACTIVITY.supportFragmentManager.popBackStack()
                }
            }
        }
    }
}