package ru.renett.api.youtube.models.playlist;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResourceId{

	@SerializedName("kind")
	private String kind;

	@SerializedName("videoId")
	private String videoId;
}