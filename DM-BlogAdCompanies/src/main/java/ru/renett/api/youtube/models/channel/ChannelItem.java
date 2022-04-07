package ru.renett.api.youtube.models.channel;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChannelItem {

	@SerializedName("id")
	private String id;

	@SerializedName("snippet")
	private ChannelSnippet snippet;

	@SerializedName("contentDetails")
	private ContentDetails contentDetails;

	@SerializedName("statistics")
	private Statistics statistics;
}