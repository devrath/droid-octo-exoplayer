package com.example.code.exoplayer.util.playlists

object PlayList {

    /**
     * ********************************** List of HLS items *********************************
     */
    fun hlsList(): ArrayList<PlayListItem> {

        val listOfHls = ArrayList<PlayListItem>()
        listOfHls.add(
            PlayListItem(
                name = "Video-1",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-2",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-3",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-4",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-5",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-6",
                uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8"
            )
        )

        return listOfHls
    }

    /**
     * ********************************* List of DASH items *********************************
     */
    fun dashItemList(): ArrayList<PlayListItem> {

        val listOfHls = ArrayList<PlayListItem>()
        listOfHls.add(
            PlayListItem(
                name = "Video-1",
                uri = "https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-2",
                uri = "https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-3",
                uri = "https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,webm2_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=249B04F79E984D7F86B4D8DB48AE6FAF41C17AB3.7B9F0EC0505E1566E59B8E488E9419F253DDF413&key=ik0"
            )
        )
        listOfHls.add(
            PlayListItem(
                name = "Video-4",
                uri = "https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,webm2_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=B1C2A74783AC1CC4865EB312D7DD2D48230CC9FD.BD153B9882175F1F94BFE5141A5482313EA38E8D&key=ik0"
            )
        )

        return listOfHls
    }


}