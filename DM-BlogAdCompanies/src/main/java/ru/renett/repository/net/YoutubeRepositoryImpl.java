package ru.renett.repository.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.renett.api.YoutubeApi;
import ru.renett.api.youtube.mapper.YoutubeMapper;
import ru.renett.api.youtube.models.channel.YoutubeChannelResponse;
import ru.renett.api.youtube.models.playlist.PlaylistItemsResponse;
import ru.renett.api.youtube.models.search.YoutubeSearchResponse;
import ru.renett.api.youtube.models.video.YoutubeVideoResponse;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Channel;
import ru.renett.models.Video;

import java.io.IOException;
import java.util.List;

import static ru.renett.api.YoutubeApi.BASE_URL;

@Repository
public class YoutubeRepositoryImpl implements YoutubeRepository {
    private final Retrofit retrofit;
    private final OkHttpClient client;
    private final YoutubeApi api;
    private final YoutubeMapper mapper;

    @Autowired
    public YoutubeRepositoryImpl(YoutubeMapper mapper, String youtubeApiKey) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request origRequest = chain.request();
                        HttpUrl newUrl = origRequest.url().newBuilder()
                                .addQueryParameter("key", youtubeApiKey)
                                .build();
                        return chain.proceed(origRequest.
                                newBuilder()
                                .url(newUrl)
                                .build());
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(YoutubeApi.class);

        this.mapper = mapper;
    }

    @Override
    public List<Video> getVideosByPlaylistId(String playlistId) {
        Call<PlaylistItemsResponse> responseCall = api.getPlaylistItemsByPlaylistId(playlistId);
        try {
            PlaylistItemsResponse response = responseCall.execute().body();
            List<Video> videoList = mapper.mapToVideoList(response);
            System.out.println(videoList.size());
            System.out.println("next page - " + response.getNextPageToken());

            while (response != null & response.getNextPageToken() != null) {
                response = api.getPlaylistItemsByPlaylistIdNextPage(playlistId, response.getNextPageToken()).execute().body();
                videoList.addAll(mapper.mapToVideoList(response));
                System.out.println(videoList.size());
                System.out.println(response.getNextPageToken());
            }
            return videoList;
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public Channel getChannelById(String id) {
        Call<YoutubeChannelResponse> responseCall = api.getChannelById(id);
        try {
            return mapper.mapToChannel(responseCall.execute().body());
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public Channel getChannelByUserName(String forUsername) {
        Call<YoutubeChannelResponse> responseCall = api.getChannelByUserName(forUsername);
        try {
            return mapper.mapToChannel(responseCall.execute().body());
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public List<Channel> getChannelsBySearchQuery(String q) {
        Call<YoutubeSearchResponse> responseCall = api.getChannelsBySearchQuery(q);
        try {
            return mapper.mapToChannelList(responseCall.execute().body());
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }

    @Override
    public Video getVideoById(String youtubeId) {
        Call<YoutubeVideoResponse> responseCall = api.getVideoById(youtubeId);
        try {
            return mapper.mapToVideo(responseCall.execute().body());
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }
}
