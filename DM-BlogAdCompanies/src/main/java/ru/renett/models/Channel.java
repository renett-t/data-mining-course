package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@Builder
public class Channel {
    private String youtubeId;
    private String userName;
    private String description;
    private String thumbnailUrl;
    private String uploadsPlaylistId;
    private String givenURI;

    private BlogStatistics statistics;
}
