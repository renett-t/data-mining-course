package ru.renett.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.renett.models.Link;
import ru.renett.models.Video;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoLinksAnalyzerImpl implements VideoLinksAnalyzer {
    private final UriScanner uriScanner;

    @Autowired
    public VideoLinksAnalyzerImpl(UriScanner uriScanner) {
        this.uriScanner = uriScanner;
    }

    @Override
    public List<Link> getAllLinksFromVideos(List<Video> videos) {
        List<Link> links = new ArrayList<>();

        for (Video video : videos) {
            List<String> strings = uriScanner.getUriFromString(video.getDescription());
            System.out.println("-- in video " + video.getTitle() + " links found: " + strings.size());

            for (String link : strings) {
                Link newLink = Link.builder()
                        .videoId(video.getYoutubeId())
                        .channelId(video.getOwnerId())
                        .rawValue(link)
                        .build();
                links.add(newLink);
            }
        }
        return links;
    }
}
