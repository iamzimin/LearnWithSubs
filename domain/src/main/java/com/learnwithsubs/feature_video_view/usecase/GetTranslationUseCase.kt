package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.repository.YandexTranslatorRepository
import com.learnwithsubs.feature_video_view.service.ServerTimeService
import retrofit2.awaitResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val TIMER_INTERVAL: Long =  10 * 60 * 60

class GetTranslationUseCase(
    private val yandexTranslatorRepository: YandexTranslatorRepository,
    private val serverTimeService: ServerTimeService
) {
    suspend fun invoke(word: String, learnLanguage: Pair<String, String>): String? {
        val yandexTranslatorBody = YandexTranslatorBody(
            targetLanguageCode = learnLanguage.second,
            texts = word,
            folderId = TranslationKeyAPI.YANDEX_FOLDER_ID
        )

        val currentTime = serverTimeService.getCurrentTime().awaitResponse()
        val dateTime = if (currentTime.isSuccessful) {
            val bdy = currentTime.body()
            if (bdy != null) {
                LocalDateTime.of(bdy.year, bdy.month, bdy.day, bdy.hour, bdy.minute, bdy.seconds).toEpochSecond(ZoneOffset.UTC)
            } else Long.MAX_VALUE
        } else Long.MAX_VALUE

        var yandexIAmToken = yandexTranslatorRepository.getYandexIAmToken()
        if (dateTime - yandexTranslatorRepository.getLastUpdateTimeYandexIAmToken() >= TIMER_INTERVAL) {
            val yandexIAmBodyRequest = YandexIAmBodyRequest(yandexPassportOauthToken = TranslationKeyAPI.YANDEX_OAUTH)
            val iAmTokenResponse = yandexTranslatorRepository.getYandexIAmToken(body = yandexIAmBodyRequest).awaitResponse()
            if (iAmTokenResponse.isSuccessful) {
                val responseBody = iAmTokenResponse.body()
                if (responseBody != null) {
                    yandexIAmToken = responseBody.iamToken
                    yandexTranslatorRepository.saveYandexIAmToken(iamtoken = responseBody.iamToken)
                    yandexTranslatorRepository.saveLastUpdateTimeYandexIAmToken(time = dateTime)
                }
            }
        }

        if (yandexIAmToken == null) // TODO if null
            return null

        val serviceType = "Yandex"
        return when (serviceType) {
            "Yandex" -> yandex(contentType = "application/json", authorization = "Bearer $yandexIAmToken", body = yandexTranslatorBody)
            else -> return null
        }
    }

    private suspend fun yandex(contentType: String, authorization: String, body: YandexTranslatorBody): String? {
        val yandexResponse = yandexTranslatorRepository.getYandexTranslation(contentType = contentType, authorization = authorization, body = body).awaitResponse()
        return if (yandexResponse.isSuccessful) {
            val apiResponse = yandexResponse.body()
            val translationList = apiResponse?.translations
            translationList?.get(0)?.text
        } else null
    }
}