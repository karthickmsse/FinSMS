package com.kardev.finsms.feature.smsingest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val repository = entryPoint(context).smsRepository()

        messages.forEach { sms ->
            val sender = sms.originatingAddress ?: return@forEach
            val body = sms.messageBody ?: return@forEach
            val timestamp = sms.timestampMillis

            if (!SenderFilter.isBankOrUpiSender(sender)) return@forEach

            CoroutineScope(Dispatchers.IO).launch {
                repository.ingestSms(sender, body, timestamp)
            }
        }
    }

    private fun entryPoint(@ApplicationContext context: Context): SmsIngestEntryPoint =
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SmsIngestEntryPoint::class.java
        )
}
