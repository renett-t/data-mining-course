package ru.renett.api.youtube.models.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics{
	private String likeCount;
	private String viewCount;
	private String commentCount;
}
