package ru.planirui.transmit.ui.screens.goods

import android.os.Bundle
import android.view.View
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.databinding.FragmentChangeBioBinding
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения описания товара, если товара не было, то с добавлением всех недостающих полей */

class ChangeGoodsFragment(
    private val description: String,
    private var idGoods: String,
    private val changeName: String
) : BaseChangeFragment(R.layout.fragment_change_bio) {

    private var binding: FragmentChangeBioBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeBioBinding.bind(view)
        initFields()
    }

    private fun initFields() {
        binding?.settingsInputBio?.setText(description)
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
        val newDescriptions = binding?.settingsInputBio?.text.toString()
        setGoodsChangeToDatabase(newDescriptions, idGoods, changeName)
    }
}