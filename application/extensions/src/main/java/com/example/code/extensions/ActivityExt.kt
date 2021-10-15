package com.example.code.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat

inline fun <reified T : Any> Activity.launchActivity (
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {})
{
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity (
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {})
{
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

/*******************************************************************************************
// Simple Activities
launchActivity<UserDetailActivity>()
********************************************************************************************
// Add Intent extras
launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
}
*********************************************************************************************
// Add custom flags
launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}
*********************************************************************************************
// Add Shared Transistions
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, avatar, "avatar")
launchActivity<UserDetailActivity>(options = options) {
    putExtra(INTENT_USER_ID, user.id)
}
*********************************************************************************************
// Add requestCode for startActivityForResult() call
launchActivity<UserDetailActivity>(requestCode = 1234) {
    putExtra(INTENT_USER_ID, user.id)
}
*********************************************************************************************
*/
