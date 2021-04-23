package ru.planirui.transmit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.planirui.transmit.model.DataModel

/*
 *      MainViewModel
 *      - viewModel that updates the MainFragment (the visible UI)
 *      - gets the data from model
 */
class MainViewModel: ViewModel() {

    // Create the model which contains data for our UI
    private var model = DataModel(textForUI = "Here's the updated text!")

    // Create MutableLiveData which MainFragment can subscribe to
    // When this data changes, it triggers the UI to do an update
    val uiTextLiveData = MutableLiveData<String>()

    // Get the updated text from our model and post the value to MainFragment
    fun getUpdatedText(number: Number) {
        when (number) {
            0 -> model = DataModel(textForUI = "Here's the updated text! $number")
            1 -> model = DataModel(textForUI = "Мы нажали на кнопку 1")
            2 -> model = DataModel(textForUI = "Кликнули по кнопке 2")
            3 -> model = DataModel(textForUI = "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю." +
                    "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю." +
                    "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю." +
                    "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю." +
                    "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю." +
                    "Активована кнопка 3. Давайте напишем тут очень большое сообщение и посмотрим, что будет происходить. Наверняка текст залезет под кнопку, но я этого пока ещё не знаю.")
            else -> model = DataModel(textForUI = "Ничего не понимаю")
        }
        val updatedText = model.textForUI
        uiTextLiveData.postValue(updatedText)
    }
}
