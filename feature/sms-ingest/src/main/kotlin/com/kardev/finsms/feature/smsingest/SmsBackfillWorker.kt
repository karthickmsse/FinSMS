package com.kardev.finsms.feature.smsingest

import android.content.Context
import android.provider.Telephony
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SmsBackfillWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: SmsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val cursor = applicationContext.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE),
            null, null,
            "${Telephony.Sms.DATE} DESC"
        ) ?: return Result.failure()

        cursor.use {
            val addressIdx = it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyIdx = it.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val dateIdx = it.getColumnIndexOrThrow(Telephony.Sms.DATE)

            while (it.moveToNext()) {
                val sender = it.getString(addressIdx) ?: continue
                if (!SenderFilter.isBankOrUpiSender(sender)) continue

                repository.ingestSms(
                    sender = sender,
                    body = it.getString(bodyIdx) ?: continue,
                    timestamp = it.getLong(dateIdx)
                )
            }
        }
        return Result.success()
    }

    companion object {
        fun trigger(context: Context) {
            val request = OneTimeWorkRequestBuilder<SmsBackfillWorker>().build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
