package com.igorvd.githubrepo.utils.extensions

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.igorvd.githubrepo.utils.CircleTransform
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

/**
 * Contains extension for the [View] class and subclasses
 * @author Igor Vilela
 * @since 16/10/17
 */


/**
 * Loads a image from URL using the [Picasso] library. It transforms the image into a circle,
 * using the [CircleTransform] class
 * @param[url] - The url for the image to be loaded
 * @param[placeholderRes] - An optional drawable res to be used as image placeholder while it is loading
 * @param[errorRes] - An optional drawable res to be used as image error placeholder if the image wasn't
 *              loaded properly
 */
fun ImageView.loadImageFromUrl(url: String,
                               @DrawableRes placeholderRes: Int = -1,
                               @DrawableRes errorRes: Int = -1) {
    val rc :RequestCreator = Picasso.with(context).
            load(url)
            .transform(CircleTransform())
            .fit()

    if(placeholderRes != -1) rc.placeholder(placeholderRes)
    if(errorRes != -1) rc.error(errorRes)

    rc.into(this)

}