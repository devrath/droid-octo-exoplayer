package com.example.code.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.popBackStack() {
    this.hideSoftKeyboard()
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popBackStackInclusive() {
    this.hideSoftKeyboard()
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(supportFragmentManager.getBackStackEntryAt(0).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) replace(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment, fragment.javaClass.simpleName)
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, addToStack: Boolean, clearBackStack: Boolean) {
    supportFragmentManager.inTransaction {

        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        if (addToStack) replace(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment, fragment.javaClass.simpleName)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) add(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else add(frameId, fragment)
    }
}


fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""

    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name

    return fragmentManager.findFragmentByTag(fragmentTag)
}