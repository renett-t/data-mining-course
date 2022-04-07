package ru.renett.api.youtube.models.channel;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ContentDetails {
	@SerializedName("relatedPlaylists")
	private RelatedPlaylists relatedPlaylists;
}