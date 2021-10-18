package com.mpl.androidapp.kotlin.viewAction

sealed class StreamEndedAction {

    object SeeMore : StreamEndedAction()
    object ContinueWatching : StreamEndedAction()

}
