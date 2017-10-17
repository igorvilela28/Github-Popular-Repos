package com.igorvd.githubrepo.utils.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Igor Vilela
 * @since 17/10/17
 */


fun String.formatDate() : String {

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", getPtBrLocale())

    try {
        val dt = sdf.parse(this)
        val sdf2 = SimpleDateFormat("dd/MM/yyyy", getPtBrLocale())

        return sdf2.format(dt)

    } catch (e: ParseException) {
        return ""
    }
}

fun getPtBrLocale() : Locale = Locale("pt", "BR")