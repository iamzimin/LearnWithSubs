package com.learnwithsubs.feature_video_view.usecase

import com.learnwithsubs.feature_video_view.TranslationKeyAPI
import com.learnwithsubs.feature_video_view.models.TranslationModel
import com.learnwithsubs.feature_video_view.models.server.YandexIAmBodyRequest
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorBody
import com.learnwithsubs.feature_video_view.models.server.YandexTranslatorResponse
import com.learnwithsubs.feature_video_view.repository.TranslatorRepository
import com.learnwithsubs.feature_video_view.repository.YandexTokenRepository
import com.learnwithsubs.feature_video_view.service.ServerTimeService
import retrofit2.awaitResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val TIMER_INTERVAL: Long =  10 * 60 * 60

class GetYandexTranslationUseCase(
    private val translatorRepository: TranslatorRepository<YandexTranslatorResponse>,
    private val yandexTokenRepository: YandexTokenRepository,
    private val serverTimeService: ServerTimeService
) {
    suspend fun invoke(model: TranslationModel): String? {
        val yandexTranslatorBody = YandexTranslatorBody(
            targetLanguageCode = model.learnLanguage_ISO639_1,
            texts = model.word,
            folderId = TranslationKeyAPI.YANDEX_FOLDER_ID
        )

        val currentTime = serverTimeService.getCurrentTime().awaitResponse()
        val dateTime = if (currentTime.isSuccessful) {
            val bdy = currentTime.body()
            if (bdy != null) {
                LocalDateTime.of(bdy.year, bdy.month, bdy.day, bdy.hour, bdy.minute, bdy.seconds)
                    .toEpochSecond(ZoneOffset.UTC)
            } else Long.MAX_VALUE
        } else Long.MAX_VALUE

        var yandexIAmToken = yandexTokenRepository.getYandexIAmToken()
        if (dateTime - yandexTokenRepository.getLastUpdateTimeYandexIAmToken() >= TIMER_INTERVAL) {
            val yandexIAmBodyRequest =
                YandexIAmBodyRequest(yandexPassportOauthToken = TranslationKeyAPI.YANDEX_OAUTH)
            val iAmTokenResponse =
                yandexTokenRepository.getYandexIAmToken(body = yandexIAmBodyRequest).awaitResponse()
            if (iAmTokenResponse.isSuccessful) {
                val responseBody = iAmTokenResponse.body()
                if (responseBody != null) {
                    yandexIAmToken = responseBody.iamToken
                    yandexTokenRepository.saveYandexIAmToken(iamtoken = responseBody.iamToken)
                    yandexTokenRepository.saveLastUpdateTimeYandexIAmToken(time = dateTime)
                }
            }
        }

        if (yandexIAmToken == null)
            return null

        val yandexResponse = translatorRepository.getTranslation(
            contentType = "application/json",
            authorization = "Bearer $yandexIAmToken",
            body = yandexTranslatorBody
        ).awaitResponse()
        return if (yandexResponse.isSuccessful) {
            val apiResponse = yandexResponse.body()
            val translationList = apiResponse?.translations
            translationList?.get(0)?.text
        } else null
    }
}