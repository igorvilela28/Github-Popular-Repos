package com.igorvd.githubrepo.utils.extensions

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.igorvd.githubrepo.utils.CircleTransform
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

/**
 * Contain extension for [View] objects
 * @author Igor Vilela
 * @since 16/10/17
 */



fun ImageView.loadImageFromUrl(url: String,
                               @DrawableRes placeholder: Int = -1,
                               @DrawableRes error: Int = -1) {
    val rc :RequestCreator = Picasso.with(context).
            load(url)
            .transform(CircleTransform())

    if(placeholder != -1) rc.placeholder(placeholder)
    if(error != -1) rc.error(error)

    rc.into(this)

}