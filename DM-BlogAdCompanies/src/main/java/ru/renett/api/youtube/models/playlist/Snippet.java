package ru.renett.api.youtube.models.playlist;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Snippet {
	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("channelId")
	private String channelId;

	@SerializedName("title")
	private String title;

	@SerializedName("description")
	private String description;

	@SerializedName("thumbnails")
	private Thumbnails thumbnails;

	@SerializedName("channelTitle")
	private String channelTitle;

	@SerializedName("playlistId")
	private String playlistId;

	@SerializedName("position")
	private int position;

	@SerializedName("resourceId")
	private ResourceId resourceId;

	@SerializedName("videoOwnerChannelTitle")
	private String videoOwnerChannelTitle;

	@SerializedName("videoOwnerChannelId")
	private String videoOwnerChannelId;
}