package ru.planirui.transmit.ui.fragments

import android.util.Log
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.*

class MyGamesFragment : BaseFragment(R.layout.fragment_my_games) {
    private val TAG = "MyGamesFragment"

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.action_my_games)

        val dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_ID] = "123123123123"
        dateMap[CHILD_PHONE] = "+79030101011"
        dateMap[CHILD_USERNAME] = "Вася"

        Log.d(TAG, dateMap.toString())

        REF_DATABASE_ROOT.child(NODE_USERS).child(dateMap[CHILD_ID].toString()).updateChildren(dateMap)
            // не срабатывает вообще никак. думаю, что трабл в REF_DATABASE_ROOT
            .addOnCompleteListener { task2 ->
                if (task2.isSuccessful) {
                    Log.d(TAG, "REF ok")
                    showToast("Добро пожаловать")
                } else Log.d(TAG, "Ref error: $task2.exception?.message.toString()")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
            .addOnCanceledListener {
                Log.d(TAG, "как мы сюда попали?")
            }
    }
}