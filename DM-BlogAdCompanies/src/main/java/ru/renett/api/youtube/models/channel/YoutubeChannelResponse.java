package ru.renett.api.youtube.models.channel;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.renett.api.youtube.models.playlist.PageInfo;

@AllArgsConstructor
@Data
public class YoutubeChannelResponse{
	@SerializedName("pageInfo")
	private PageInfo pageInfo;

	@SerializedName("items")
	private List<ChannelItem> items;
}