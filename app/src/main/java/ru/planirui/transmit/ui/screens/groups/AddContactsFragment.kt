package ru.planirui.transmit.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_add_contacts.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.initRVAddContact3
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.hideKeyboard
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участника"
        hideKeyboard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участника")
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycle_view
        mAdapter = AddContactsAdapter()
        initRVAddContact3() { tempList, newModel ->
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