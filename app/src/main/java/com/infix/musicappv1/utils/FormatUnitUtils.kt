package com.infix.musicappv1.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatUnitUtils {
    fun toVndFormatted(price: Double, suffix: String = "VNĐ"): String {
        val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val formatter = DecimalFormat("#,###", symbols)
        return "${formatter.format(price)}"
    }
}