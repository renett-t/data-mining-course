package ru.renett.api.youtube.models.playlist;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlaylistItemsResponse {

	@SerializedName("nextPageToken")
	private String nextPageToken;

	@SerializedName("prevPageToken")
	private String prevPageToken;

	@SerializedName("pageInfo")
	private PageInfo pageInfo;

	@SerializedName("items")
	private List<PlaylistItem> items;
}