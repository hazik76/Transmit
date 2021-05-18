package ru.planirui.transmit.ui.screens.goods

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения описания товара, если товара не было, то с добавлением всех недостающих полей */

class ChangeGoodsFragment(
    private val description: String,
    private var idGoods: String,
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
        if (idGoods == ""){
            newGoodsCreate {
                idGoods = it
                USER.tempMessage = it
                change2()
            }
        }else change2()
    }

    private fun change2() {
        val newDescriptions = settings_input_bio.text.toString()
        setGoodsChangeToDatabase(newDescriptions, idGoods, changeName)
//        when(changeName){
//            "description" -> setGoodsDescriptionsToDatabase(newDescriptions, idGoods)
//            "extend" ->  setGoodsExtendToDatabase(newDescriptions, idGoods)
//            "name" ->  setGoodsNameToDatabase(newDescriptions, idGoods)
//        }
    }
}