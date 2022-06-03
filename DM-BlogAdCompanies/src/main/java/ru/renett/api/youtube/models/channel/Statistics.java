package ru.renett.api.youtube.models.channel;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Statistics{

	@SerializedName("videoCount")
	private String videoCount;

	@SerializedName("subscriberCount")
	private String subscriberCount;

	@SerializedName("hiddenSubscriberCount")
	private boolean hiddenSubscriberCount;

	@SerializedName("viewCount")
	private String viewCount;
}