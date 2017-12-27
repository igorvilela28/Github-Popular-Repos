package com.igorvd.githubrepo.domain.exceptions

/**
 * A subclass of this exception should be thrown when some repository fails executing
 * the requested operation
 * @param message The message for the [Exception]
 * @author Igor Vilela
 * @since 13/10/17
 */
abstract class RepositoryException : Exception {

    constructor(t: Throwable) : super(t)
    constructor(message: String) : super(message)

}