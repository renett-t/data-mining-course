package ru.renett.api.youtube.mapper;

import org.springframework.stereotype.Component;
import ru.renett.api.youtube.models.channel.ChannelItem;
import ru.renett.api.youtube.models.channel.YoutubeChannelResponse;
import ru.renett.api.youtube.models.playlist.PlaylistItem;
import ru.renett.api.youtube.models.playlist.PlaylistItemsResponse;
import ru.renett.api.youtube.models.playlist.Snippet;
import ru.renett.api.youtube.models.search.SearchItem;
import ru.renett.api.youtube.models.search.YoutubeSearchResponse;
import ru.renett.api.youtube.models.video.VideoItem;
import ru.renett.api.youtube.models.video.YoutubeVideoResponse;
import ru.renett.models.BlogStatistics;
import ru.renett.models.Channel;
import ru.renett.models.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class YoutubeMapperImpl implements YoutubeMapper {
    private String VIDEO_TAG = "youtube#video";
    private String CHANNEL_TAG = "youtube#channel";

    @Override
    public List<Video> mapToVideoList(PlaylistItemsResponse playlistItemsResponse) {
        List<Video> videoList = new ArrayList<>();
        for (PlaylistItem item : playlistItemsResponse.getItems()) {
            Snippet snippet = item.getSnippet();
            if (Objects.equals(snippet.getResourceId().getKind(), VIDEO_TAG)) {

                Video newVideo = Video.builder()
                        .youtubeId(snippet.getResourceId().getVideoId())
                        .ownerId(snippet.getVideoOwnerChannelId())
                        .title(snippet.getTitle())
                        .description(snippet.getDescription())
                        .thumbnailUrl(snippet.getThumbnails().getHigh().getUrl())
                        .build();

                videoList.add(newVideo);
            }
        }
        return videoList;
    }

    @Override
    public Channel mapToChannel(YoutubeChannelResponse body) {
//        if (body.getPageInfo().getTotalResults() > 1) {
//            todo: throw неправильныйЗапросЭксепшен if results > 1
//        }
        ChannelItem item = body.getItems().get(0);

        BlogStatistics blogStatistics = BlogStatistics.builder()
                .ownerId(item.getId())
                .viewCount(Long.valueOf(item.getStatistics().getViewCount()))
                .subscriberCount(Long.valueOf(item.getStatistics().getSubscriberCount()))
                .videoCount(Long.valueOf(item.getStatistics().getVideoCount()))
                .build();

        return Channel.builder()
                .youtubeId(item.getId())
                .userName(item.getSnippet().getTitle())
                .description(item.getSnippet().getDescription())
                .thumbnailUrl(item.getSnippet().getThumbnails().getHigh().getUrl())
                .uploadsPlaylistId(item.getContentDetails().getRelatedPlaylists().getUploads())
                .statistics(blogStatistics)
                .build();
    }

    @Override
    public List<Channel> mapToChannelList(YoutubeSearchResponse body) {
        List<Channel> channelList = new ArrayList<>();
        for (SearchItem item : body.getItems()) {
            if (item.getItemId().getKind().equals(CHANNEL_TAG)) {
                ru.renett.api.youtube.models.search.Snippet snippet = item.getSnippet();
                Channel newChannel = Channel.builder()
                        .youtubeId(snippet.getChannelId())
                        .userName(snippet.getChannelTitle())
                        .description(snippet.getDescription())
                        .thumbnailUrl(snippet.getThumbnails().getHigh().getUrl())
                        .build();
                channelList.add(newChannel);
            }
        }
        return channelList;
    }

    @Override
    public Video mapToVideo(YoutubeVideoResponse body) {
        VideoItem item = body.getItems().get(0);

        long views = 0L;
        long likes = 0L;
        long comments = 0L;

        try {
            views = Long.parseLong(item.getStatistics().getViewCount());
        } catch (Throwable ignored) {
        }
        try {
            likes = Long.parseLong(item.getStatistics().getLikeCount());
        } catch (Throwable ignored) {
        }
        try {
            comments = Long.parseLong(item.getStatistics().getCommentCount());
        } catch (Throwable ignored) {
        }

        return Video.builder()
                .youtubeId(item.getId())
                .ownerId(item.getSnippet().getChannelId())
                .title(item.getSnippet().getTitle())
                .description(item.getSnippet().getDescription())
                .thumbnailUrl(item.getSnippet().getThumbnails().getHigh().getUrl())
                .viewCount(views)
                .likeCount(likes)
                .commentCount(comments)
                .build();
    }
}
