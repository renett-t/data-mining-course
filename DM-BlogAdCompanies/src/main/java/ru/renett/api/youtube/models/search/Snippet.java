package ru.renett.api.youtube.models.search;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.renett.api.youtube.models.playlist.Thumbnails;

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
}