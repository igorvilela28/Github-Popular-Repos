package com.igorvd.githubrepo.utils.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Contain exceptions to use when dealing with objects that represent dates/times
 * @author Igor Vilela
 * @since 17/10/17
 */

/**
 * format the String in the format [yyyy-MM-dd'T'HH:mm:ss'Z'] to the format [dd/MM/yyyy].
 * returns an empty string if the parsing fails
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