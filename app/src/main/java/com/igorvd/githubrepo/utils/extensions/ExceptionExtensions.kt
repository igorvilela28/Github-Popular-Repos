package com.igorvd.githubrepo.utils.extensions

import com.igorvd.githubrepo.BuildConfig

/**
 * Contains extensions for the [Exception] class and sub-classes
 * @author Igoir Vilela
 * @since 16/10/17
 */


/**
 * Throws the exception if [BuildConfig.DEBUG] or log it in our crash-report tool otherwise.
 * This is useful in order to find bugs fast while development
 */
fun Exception.throwOrLog() {

    if(BuildConfig.DEBUG) {
        throw this
    } else {

        //TODO: add some crash-reporting tool log for the exception
    }
}