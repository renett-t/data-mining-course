package ru.renett.api.youtube.models.playlist;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlaylistItem {
	@SerializedName("id")
	private String id;

	@SerializedName("snippet")
	private Snippet snippet;
}