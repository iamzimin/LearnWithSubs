package com.learnwithsubs.feature_video_view.usecase

import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.YandexIAmBodyResponse
import com.learnwithsubs.feature_video_view.models.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GetTranslationUseCase(
    private val yandexDictionaryRepository: TranslatorRepository<YandexTranslatorResponse>
) {
    val translationLiveData: MutableLiveData<String?> = MutableLiveData()

    suspend fun invoke(word: String, learnLanguage: Pair<String, String>) {
        val body = YandexTranslatorBody(
            targetLanguageCode = learnLanguage.second,
            texts = word,
            folderId = TranslationKeyAPI.YANDEX_FOLDER_ID
        )

        val yandexContentType = "application/json"
        val yandexIAmBodyRequest = YandexIAmBodyRequest(yandexPassportOauthToken = TranslationKeyAPI.YANDEX_OAUTH)

        var authorization: String? = null // TODO if null
        val response = yandexDictionaryRepository.getYandexIAmToken(body = yandexIAmBodyRequest).awaitResponse()
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null)
                authorization = "Bearer ${responseBody.iamToken}"
        } else {
            response.errorBody()
        }


        val test = "Yandex"
        when (test) {
            "Yandex" -> authorization?.let { yandex(contentType = yandexContentType, authorization = it, body = body) }
        }

    }

    private fun yandex(contentType: String, authorization: String, body: YandexTranslatorBody) {
        yandexDictionaryRepository.getTranslation(
            contentType = contentType,
            authorization = authorization,
            body = body
        ).enqueue(object :
            Callback<YandexTranslatorResponse> {
            override fun onResponse(call: Call<YandexTranslatorResponse>, response: Response<YandexTranslatorResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val translationList = apiResponse?.translations
                    val translation = translationList?.get(0)?.text
                    translationLiveData.value = translation

                } else {
                    val tests = 1 // TODO
                }
            }

            override fun onFailure(call: Call<YandexTranslatorResponse>, t: Throwable) {
                val tests = 1 // TODO
            }

        })
    }
}