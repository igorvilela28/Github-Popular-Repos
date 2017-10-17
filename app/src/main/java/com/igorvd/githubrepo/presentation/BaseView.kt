package com.igorvd.githubrepo.presentation

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
interface BaseView {

    /**
     * Show a loading progress in the view
     */
    fun showProgress()

    /**
     * Hide the loading progress in the view
     */
    fun hideProgress()

    /**
     * Show a error message in the view
     */
    fun showError()
}