package ru.renett.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Playlist {
    private String youtubeId;
    private String ownerId;
    private List<Video> videos;
}
