package ru.planirui.transmit.ui.screens.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ru.planirui.transmit.R
import ru.planirui.transmit.database.createGroupToDatabase
import ru.planirui.transmit.databinding.FragmentCreateGroupBinding
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.ui.screens.main_list.MainListFragment
import ru.planirui.transmit.utilits.*

class CreateGroupFragment(private var listContacts:List<CommonModel>): BaseFragment(R.layout.fragment_create_group) {

    private var binding: FragmentCreateGroupBinding? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mUri = Uri.EMPTY

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateGroupBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        binding?.createGroupPhoto?.setOnClickListener { addPhoto()  }
        binding?.createGroupBtnComplete?.setOnClickListener {
            val nameGroup = binding?.createGroupInputName?.text.toString()
            if (nameGroup.isEmpty()){
                showToast("Введите имя")
            } else {
                val uriGoods = "вытаскивать у пользователя"
                val nameGoods = "получать от товара"
                val photoGoods = "ссылка на картинку товара"
                val regionGoods = "Например Москва"
                val timeEnd = "1621437897911"   // добавить выбор даты
                createGroupToDatabase(nameGroup, mUri, timeEnd, uriGoods, nameGoods, regionGoods, photoGoods, listContacts){
                    replaceFragment(MainListFragment())
                }
            }
        }
        binding?.createGroupInputName?.requestFocus()
        binding?.createGroupCounts?.text = getPlurals(listContacts.size)
    }

    private fun addPhoto() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY,this)
    }

    private fun initRecyclerView() {
        mRecyclerView = binding?.createGroupRecycleView!!
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach {  mAdapter.updateListItems(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность которая запускается для получения картинки для фото группы */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            mUri = CropImage.getActivityResult(data).uri
            binding?.createGroupPhoto?.setImageURI(mUri)
        }
    }
}