package ru.renett.api.youtube.models.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoItem {
	private String id;
	private Snippet snippet;
	private Statistics statistics;
}
