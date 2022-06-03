package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BlackListedLink {
    protected Long id;
    protected String channelId;
    protected String value;
}
