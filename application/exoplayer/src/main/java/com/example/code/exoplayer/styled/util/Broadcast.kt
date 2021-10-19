package com.example.code.exoplayer.styled.util


data class Broadcast(
    val gameBroadcastName: String = "Dummy Broadcast Name",
    val koTournamentId: Int = 1,
    val displayGameName: String = "Dummy game name",
    val displayRoundName: String = "Round-1",
    val displayStageName: String = "Stage-1",
    val displayTournamentName: String = "Dummy Tournament",
    val durationInMils: Long = 0,
    var gameBroadcastId: String ="1111111",
    val gameIcon: String = "",
    val hostedBy: String = "",
    val liveUrl: String = "",
    val liveViewCount: Int = 10,
    val reminderOn: Boolean = false,
    var status: String,
    val startTime: Long = 0,
    val thumbnailUrl: String= "",
    val totalHeartCount: Int = 10,
    val totalShareCount: Int = 20,
    val totalViewCount: Int = 30,
    var vodUrl: String = "",
    val tileUrl: String = ""
) {
    companion object {
        const val STATUS_VOD = "VOD"
        const val STATUS_LIVE = "LIVE"
        const val STATUS_UPCOMING = "UPCOMING"
    }
    fun isVOD(): Boolean = (status == STATUS_VOD)
    fun isLive(): Boolean = (status == STATUS_LIVE)
    fun isUpcoming(): Boolean = (status == STATUS_UPCOMING)
}