package com.igorvd.githubrepo.utils.extensions

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.view.View.generateViewId



/**
 * @author Igor Vilela
 * @since 16/10/17
 */

fun ConstraintLayout.centerViewInParent(view: View) {

    val constraints = ConstraintSet()
    constraints.clone(this)

    // constrain the View so it is centered on the screen.
    // There is also a "center" method that can be used here.
    constraints.center(view.getId(), ConstraintSet.PARENT_ID, ConstraintSet.START,
            0, ConstraintSet.PARENT_ID, ConstraintSet.END, 0, 0.5f)
    constraints.center(view.getId(), ConstraintSet.PARENT_ID, ConstraintSet.TOP,
            0, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0, 0.5f)
    constraints.applyTo(this)

}

fun ConstraintLayout.centerViewBottom(view: View) {

    val constraints = ConstraintSet()
    constraints.clone(this)

  /*  // Now constrain the ImageView so it is centered on the screen.
    // There is also a "center" method that can be used here.
    constraints.center(view.getId(), ConstraintSet.PARENT_ID, ConstraintSet.START,
            0, ConstraintSet.PARENT_ID, ConstraintSet.END, 0, 0.5f)*/

    constraints.connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8)

    constraints.applyTo(this)

}