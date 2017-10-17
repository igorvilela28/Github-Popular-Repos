package com.igorvd.githubrepo.presentation

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
interface BasePresenter {

    /**
     * Detaches the view from the presenter. Useful to avoid memory leaks when the Activity/Fragment
     * gets destroyed
     */
    fun detachView()

}