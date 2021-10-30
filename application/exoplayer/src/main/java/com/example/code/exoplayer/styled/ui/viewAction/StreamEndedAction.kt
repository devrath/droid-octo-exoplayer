package com.example.code.exoplayer.styled.ui.viewAction

sealed class StreamEndedAction {

    object SeeMore : StreamEndedAction()
    object ContinueWatching : StreamEndedAction()

}
