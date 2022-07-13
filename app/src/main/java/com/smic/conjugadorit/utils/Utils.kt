package com.smic.conjugadorit.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *@autor Smogevscih Yuri
16.05.2021
 **/
//returns the active fragment
val FragmentManager.getCurrentFragment: Fragment?
    get() = this.primaryNavigationFragment?.childFragmentManager?.fragments?.first()

val View.hide: View
    get() = this.apply {
        visibility = View.INVISIBLE
    }

val View.show: View
    get() = this.apply {
        visibility = View.VISIBLE
    }
val View.gone: View
    get() = this.apply {
        visibility = View.GONE
    }
