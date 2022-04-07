package ru.renett.api.youtube.models.channel;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.renett.api.youtube.models.playlist.Thumbnails;

@AllArgsConstructor
@Data
public class ChannelSnippet {

	@SerializedName("title")
	private String title;

	@SerializedName("description")
	private String description;

	@SerializedName("customUrl")
	private String customUrl;

	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("thumbnails")
	private Thumbnails thumbnails;

	@SerializedName("country")
	private String country;

	@SerializedName("localized")
	private Localized localized;

}