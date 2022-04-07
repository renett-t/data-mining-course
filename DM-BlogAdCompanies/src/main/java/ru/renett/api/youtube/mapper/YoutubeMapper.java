package ru.renett.api.youtube.mapper;

import ru.renett.api.youtube.models.channel.YoutubeChannelResponse;
import ru.renett.api.youtube.models.playlist.PlaylistItemsResponse;
import ru.renett.api.youtube.models.search.YoutubeSearchResponse;
import ru.renett.api.youtube.models.video.YoutubeVideoResponse;
import ru.renett.models.Channel;
import ru.renett.models.Video;

import java.util.List;

public interface YoutubeMapper {
    List<Video> mapToVideoList(PlaylistItemsResponse playlistItemsResponse);
    Channel mapToChannel(YoutubeChannelResponse body);
    List<Channel> mapToChannelList(YoutubeSearchResponse body);

    Video mapToVideo(YoutubeVideoResponse body);
}
