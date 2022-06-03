package ru.renett.api.youtube.models.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Snippet{
	private String description;
	private String title;
	private Thumbnails thumbnails;
	private String channelId;
}