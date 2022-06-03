package ru.renett.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
public class Video {
    private String youtubeId;
    private String ownerId;

    private String title;
    private String description;
    private String thumbnailUrl;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    public Video(String youtubeId, String ownerId, String title, String description, String thumbnailUrl, Long viewCount, Long likeCount, Long commentCount) {
        this.youtubeId = youtubeId;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;

        if (viewCount == null) viewCount = 0L;
        if (likeCount == null) likeCount = 0L;
        if (commentCount == null) commentCount = 0L;

        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
