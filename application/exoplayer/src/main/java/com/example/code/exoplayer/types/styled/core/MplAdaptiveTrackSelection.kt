package com.example.code.exoplayer.types.styled.core

import com.example.code.exoplayer.types.styled.util.MplTrack
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.source.chunk.MediaChunk
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.common.collect.ImmutableList
import timber.log.Timber

class MplAdaptiveTrackSelection(
    group: TrackGroup,
    tracks: IntArray,
    bandwidthMeter: BandwidthMeter
) : AdaptiveTrackSelection(group, tracks, bandwidthMeter) {

    private val TAG = this.javaClass.simpleName

    private var selectedTrack: MplTrack = StyledExoPlayer.getAutoTrack()
    private var shouldDiscardBuffer = false

    /**
     * We can use trackSelector.setParameters also for limiting the maxVideoSize but it invalidates and reselects the track which results rebuffer,
     * to avoid that we have use AdaptiveTrackSelection.canSelectFormat to filter out unwanted formats.
     * Ref - https://github.com/google/ExoPlayer/issues/8898
     */
    override fun canSelectFormat(
        format: Format,
        trackBitrate: Int,
        effectiveBitrate: Long
    ): Boolean {
        /*return if (exoPlayerConfig.seamlessTrackSelection) {
            canSelectFormatForSeamlessTrackSelection(
                format,
                trackBitrate,
                effectiveBitrate,
                selectedTrack
            )
        } else {
            super.canSelectFormat(format, trackBitrate, effectiveBitrate)
        }*/
        return super.canSelectFormat(format, trackBitrate, effectiveBitrate)
    }

    private fun canSelectFormatForSeamlessTrackSelection(
        format: Format,
        trackBitrate: Int,
        effectiveBitrate: Long,
        mplTrack: MplTrack?
    ): Boolean {
        return mplTrack?.let {
            if (it.isAdaptive) {
                super.canSelectFormat(format, trackBitrate, effectiveBitrate)
            } else {
                format == it.format
            }
        } ?: run {
            super.canSelectFormat(format, trackBitrate, effectiveBitrate)
        }
    }

    private fun onTrackSelected(mplTrack: MplTrack) {
        Timber.tag(TAG).d("onTrackSelected -> mplTrack:$mplTrack");

        this.selectedTrack = mplTrack
        this.shouldDiscardBuffer = !mplTrack.isAdaptive
    }

    override fun evaluateQueueSize(
        playbackPositionUs: Long,
        queue: MutableList<out MediaChunk>
    ): Int {
        return if (shouldDiscardBuffer) {
            shouldDiscardBuffer = false
            0
        } else {
            super.evaluateQueueSize(playbackPositionUs, queue)
        }
    }

    class Factory() : AdaptiveTrackSelection.Factory() {

        private var mplAdaptiveAdaptiveTrackSelection: MplAdaptiveTrackSelection? = null

        override fun createAdaptiveTrackSelection(
            group: TrackGroup,
            tracks: IntArray,
            type: Int,
            bandwidthMeter: BandwidthMeter,
            adaptationCheckpoints: ImmutableList<AdaptationCheckpoint>
        ): AdaptiveTrackSelection {
            return createAdaptiveTrackSelection(
                group,
                tracks,
                bandwidthMeter
            )
        }

        private fun createAdaptiveTrackSelection(
            group: TrackGroup, tracks: IntArray,
            bandwidthMeter: BandwidthMeter
        ): AdaptiveTrackSelection {
            mplAdaptiveAdaptiveTrackSelection =
                mplAdaptiveAdaptiveTrackSelection ?: MplAdaptiveTrackSelection(
                    group,
                    tracks,
                    bandwidthMeter
                )
            return mplAdaptiveAdaptiveTrackSelection!!
        }

        fun selectTrackSeamlessly(mplTrack: MplTrack) {
            mplAdaptiveAdaptiveTrackSelection?.onTrackSelected(mplTrack)
        }

    }

}