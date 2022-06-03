package ru.renett.api.youtube.models.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class YoutubeVideoResponse{
	private PageInfo pageInfo;
	private List<VideoItem> items;
}