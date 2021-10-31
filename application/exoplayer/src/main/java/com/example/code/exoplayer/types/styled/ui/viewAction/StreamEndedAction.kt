package com.example.code.exoplayer.types.styled.ui.viewAction

sealed class StreamEndedAction {

    object SeeMore : StreamEndedAction()
    object ContinueWatching : StreamEndedAction()

}
