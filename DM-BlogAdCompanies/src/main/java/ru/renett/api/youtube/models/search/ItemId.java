package ru.renett.api.youtube.models.search;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemId {

	@SerializedName("kind")
	private String kind;

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("channelId")
	private String channelId;
}