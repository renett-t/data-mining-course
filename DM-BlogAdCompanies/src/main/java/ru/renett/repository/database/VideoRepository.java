package ru.renett.repository.database;

import ru.renett.models.Video;
import ru.renett.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CRUDRepository<Video> {
    Optional<Video> getVideoByYoutubeId(String youtubeId);
    List<Video> getVideosByPlaylistId(String playlistId);
    List<Video> getVideosByOwnerId(String ownerId);
}
