package com.kardev.finsms.feature.parser

import com.kardev.finsms.core.common.Direction

data class SmsTemplate(
    val id: String,
    val bankSenderPattern: Regex,
    val bodyPattern: Regex,
    val direction: Direction,
    val bankName: String
)
