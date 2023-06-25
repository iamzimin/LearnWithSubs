package com.learnwithsubs.feature_video_view.usecase

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.ServerTimeService
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import retrofit2.awaitResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

class GetTranslationUseCase(
    private val context: Context,
    private val yandexTranslatorRepository: TranslatorRepository<YandexTranslatorResponse>,
    private val serverTimeService: ServerTimeService
) {
    val translationLiveData: MutableLiveData<String?> = MutableLiveData()

    suspend fun invoke(word: String, learnLanguage: Pair<String, String>) {
        val yandexTranslatorBody = YandexTranslatorBody(
            targetLanguageCode = learnLanguage.second,
            texts = word,
            folderId = TranslationKeyAPI.YANDEX_FOLDER_ID
        )

        val timerInterval: Long = 10 * 60 * 60
        val currentTime = serverTimeService.getCurrentTime().awaitResponse()
        val dateTime = if (currentTime.isSuccessful) {
            val bdy = currentTime.body()
            if (bdy != null) {
                LocalDateTime.of(bdy.year, bdy.month, bdy.day, bdy.hour, bdy.minute, bdy.seconds).toEpochSecond(ZoneOffset.UTC)
            } else Long.MAX_VALUE
        } else Long.MAX_VALUE

        val sharedPreferences = context.getSharedPreferences("YandexIAmToken", Context.MODE_PRIVATE)
        var authorization: String? = null
        if (saveCurrentTime(sharedPreferences = sharedPreferences, dateTime = dateTime, timerInterval = timerInterval)) {
            val yandexIAmBodyRequest = YandexIAmBodyRequest(yandexPassportOauthToken = TranslationKeyAPI.YANDEX_OAUTH)
            val iAamTokenResponse = yandexTranslatorRepository.getYandexIAmToken(body = yandexIAmBodyRequest).awaitResponse()
            if (iAamTokenResponse.isSuccessful) {
                val responseBody = iAamTokenResponse.body()
                if (responseBody != null) {
                    authorization = "Bearer ${responseBody.iamToken}"
                    sharedPreferences.edit().putString("iAmToken", authorization).apply()
                    Toast.makeText(context, "New Token", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            authorization = sharedPreferences.getString("iAmToken", null)
            Toast.makeText(context, "Old Token", Toast.LENGTH_SHORT).show()
        }



        if (authorization == null) { // TODO if null
            Toast.makeText(context, "Сервак не доступен", Toast.LENGTH_SHORT).show()
            return
        }

        val serviceType = "Yandex"
        when (serviceType) {
            "Yandex" -> yandex(contentType = "application/json", authorization = authorization, body = yandexTranslatorBody)
        }
    }

    private suspend fun yandex(contentType: String, authorization: String, body: YandexTranslatorBody) {
        val yandexResponse = yandexTranslatorRepository.getTranslation(contentType = contentType, authorization = authorization, body = body).awaitResponse()
        if (yandexResponse.isSuccessful) {
            val apiResponse = yandexResponse.body()
            val translationList = apiResponse?.translations
            val translation = translationList?.get(0)?.text
            translationLiveData.value = translation
        }
    }

    private fun saveCurrentTime(sharedPreferences: SharedPreferences, dateTime: Long, timerInterval: Long): Boolean {
        val lastUpdateTime = sharedPreferences.getLong("lastUpdateTime", 0)
        return if (dateTime - lastUpdateTime >= timerInterval) {
            sharedPreferences.edit().putLong("lastUpdateTime", dateTime).apply()
            true
        } else false
    }
}