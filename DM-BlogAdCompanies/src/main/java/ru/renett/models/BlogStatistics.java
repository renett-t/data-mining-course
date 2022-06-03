package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BlogStatistics {
    private Long id;
    private String ownerId;
    private Long viewCount;
    private Long subscriberCount;
    private Long videoCount;
}
