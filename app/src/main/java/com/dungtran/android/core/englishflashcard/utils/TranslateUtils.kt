package com.dungtran.android.core.englishflashcard.utils

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TranslateUtils {
    private val sourceLanguageCode = "en"
    private val targetLanguageCode = "vi"

    private var translatorOptions: TranslatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguageCode)
        .setTargetLanguage(targetLanguageCode)
        .build()
    private var translator: Translator = Translation.getClient(translatorOptions)

    init {
        val downLoadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(downLoadConditions)
    }


    suspend fun translate(source: String): String = withContext(Dispatchers.IO) {
        val completableFuture = CompletableDeferred<String>()
        translator.translate(source)
            .addOnSuccessListener { translation ->
                completableFuture.complete(translation ?: "default")
            }
            .addOnFailureListener { exception ->
                completableFuture.complete("default")
            }
        completableFuture.await()
    }
}