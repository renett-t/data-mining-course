package ru.renett.api.youtube.models.search;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.renett.api.youtube.models.playlist.PageInfo;

@AllArgsConstructor
@Data
public class YoutubeSearchResponse{

	@SerializedName("kind")
	private String kind;

	@SerializedName("etag")
	private String etag;

	@SerializedName("regionCode")
	private String regionCode;

	@SerializedName("nextPageToken")
	private String nextPageToken;

	@SerializedName("prevPageToken")
	private String prevPageToken;

	@SerializedName("pageInfo")
	private PageInfo pageInfo;

	@SerializedName("items")
	private List<SearchItem> items;
}