package ru.planirui.transmit.ui.screens.groups

/* Добавляем пользователей из листа контактов */

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.planirui.transmit.R
import ru.planirui.transmit.database.initRVAddContact
import ru.planirui.transmit.databinding.FragmentAddContactsBinding
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.hideKeyboard
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private var binding: FragmentAddContactsBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddContactsBinding.bind(view)
    }

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участника"
        hideKeyboard()
        initRecyclerView()
        binding?.addContactsBtnNext?.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участника")
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = binding?.addContactsRecycleView!!
        mAdapter = AddContactsAdapter()
        initRVAddContact() { tempList, newModel ->
            if (tempList.isEmpty()) {
                newModel.lastMessage = "Чат очищен"
            } else {
                newModel.lastMessage = tempList[0].text
            }
            if (newModel.fullname.isEmpty()) {
                newModel.fullname = newModel.phone
            }
            mAdapter.updateListItems(newModel)
        }
        mRecyclerView.adapter = mAdapter
    }

    companion object {
        val listContacts = mutableListOf<CommonModel>()
    }
}