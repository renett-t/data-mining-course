package ru.renett.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.renett.api.youtube.models.channel.YoutubeChannelResponse;
import ru.renett.api.youtube.models.playlist.PlaylistItemsResponse;
import ru.renett.api.youtube.models.search.YoutubeSearchResponse;
import ru.renett.api.youtube.models.video.YoutubeVideoResponse;

public interface YoutubeApi {
    String BASE_URL = "https://www.googleapis.com/youtube/v3/";

    @GET("playlistItems?part=snippet&maxResults=50")
    Call<PlaylistItemsResponse> getPlaylistItemsByPlaylistId(@Query("playlistId") String playlistId);

    @GET("playlistItems?part=snippet&maxResults=50")
    Call<PlaylistItemsResponse> getPlaylistItemsByPlaylistIdNextPage(@Query("playlistId") String playlistId,
                                                                     @Query("pageToken") String nextPageToken);

    @GET("channels?part=contentDetails&part=statistics&part=snippet")
    Call<YoutubeChannelResponse> getChannelById(@Query("id") String id);

    @GET("channels?part=contentDetails&part=statistics&part=snippet")
    Call<YoutubeChannelResponse> getChannelByUserName(@Query("forUsername") String forUsername);

    @GET("search?part=snippet&maxResults=10&order=relevance&type=channels")
    Call<YoutubeSearchResponse> getChannelsBySearchQuery(@Query("q") String q);

    @GET("videos?part=snippet&part=statistics")
    Call<YoutubeVideoResponse> getVideoById(@Query("id") String id);
}
