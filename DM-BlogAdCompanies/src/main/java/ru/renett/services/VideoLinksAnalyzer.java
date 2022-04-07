package ru.renett.services;

import ru.renett.models.Link;
import ru.renett.models.Video;

import java.util.List;

public interface VideoLinksAnalyzer {
    List<Link> getAllLinksFromVideos(List<Video> videos);
}
