package ru.renett.repository.net;

import ru.renett.models.Channel;
import ru.renett.models.Video;

import java.util.List;

public interface YoutubeRepository {
    List<Video> getVideosByPlaylistId(String playlistId);
    Channel getChannelById(String id);
    Channel getChannelByUserName(String forUsername);
    List<Channel> getChannelsBySearchQuery(String q);

    Video getVideoById(String youtubeId);
}