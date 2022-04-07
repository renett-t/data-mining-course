package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Link {
    protected Long id;
    private String videoId;
    protected String channelId;
    private String rawValue;
    protected String value;
    private boolean isBlackListed;
    private Long companyId;

    public Link(String rawValue) {
        this.rawValue = rawValue;
    }
}
