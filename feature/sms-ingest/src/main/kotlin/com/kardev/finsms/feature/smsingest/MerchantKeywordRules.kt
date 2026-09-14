package com.kardev.finsms.feature.smsingest

object MerchantKeywordRules {
    val RULES: Map<String, String> = mapOf(
        "swiggy" to "Food & Dining", "zomato" to "Food & Dining",
        "dominos" to "Food & Dining", "mcdonald" to "Food & Dining",
        "starbucks" to "Food & Dining", "cafe" to "Food & Dining",
        "bigbasket" to "Groceries", "blinkit" to "Groceries",
        "zepto" to "Groceries", "dmart" to "Groceries", "grofers" to "Groceries",
        "uber" to "Transport", "ola" to "Transport", "rapido" to "Transport",
        "irctc" to "Transport", "metro" to "Transport",
        "amazon" to "Shopping", "flipkart" to "Shopping", "myntra" to "Shopping",
        "ajio" to "Shopping", "meesho" to "Shopping",
        "electricity" to "Bills & Utilities", "airtel" to "Bills & Utilities",
        "jio" to "Bills & Utilities", "vodafone" to "Bills & Utilities",
        "gas" to "Bills & Utilities", "broadband" to "Bills & Utilities",
        "netflix" to "Entertainment", "hotstar" to "Entertainment",
        "spotify" to "Entertainment", "bookmyshow" to "Entertainment",
        "petrol" to "Fuel", "indianoil" to "Fuel", "bharat petroleum" to "Fuel",
        "pharmacy" to "Health & Wellness", "apollo" to "Health & Wellness",
        "practo" to "Health & Wellness", "1mg" to "Health & Wellness",
        "salary" to "Salary/Income", "sal credit" to "Salary/Income",
        "zerodha" to "Investments", "groww" to "Investments",
        "mutual fund" to "Investments", "sip" to "Investments"
    )
}
