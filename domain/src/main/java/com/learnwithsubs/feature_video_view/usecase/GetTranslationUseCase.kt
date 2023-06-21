package com.learnwithsubs.feature_video_view.usecase

import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.TranslatorYandexBody
import com.learnwithsubs.feature_video_view.models.TranslatorYandexResponse
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetTranslationUseCase(
    private val yandexDictionaryRepository: TranslatorRepository<TranslatorYandexResponse>
) {
    val translationLiveData: MutableLiveData<String?> = MutableLiveData()

    fun invoke(word: String, learnLanguage: Pair<String, String>) {
        val body = TranslatorYandexBody(
            targetLanguageCode = learnLanguage.second,
            texts = word,
            folderId = TranslationKeyAPI.YANDEX_FOLDER_ID
        )

        yandex(body = body)
    }

    private fun yandex(body: TranslatorYandexBody) {
        yandexDictionaryRepository.getTranslation(body = body).enqueue(object :
            Callback<TranslatorYandexResponse> {
            override fun onResponse(call: Call<TranslatorYandexResponse>, response: Response<TranslatorYandexResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val translationList = apiResponse?.translations
                    val translation = translationList?.get(0)?.text
                    translationLiveData.value = translation

                } else {
                    val tests = 1 // TODO
                }
            }

            override fun onFailure(call: Call<TranslatorYandexResponse>, t: Throwable) {
                // TODO
            }

        })
    }
}