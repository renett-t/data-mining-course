package ru.renett.models.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.renett.models.Channel;
import ru.renett.models.Video;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChannelVideosAds {
    private Channel channel;
    private Map<Video, List<CompanyLink>> map;
    private List<Video> orderedVideos;
}
