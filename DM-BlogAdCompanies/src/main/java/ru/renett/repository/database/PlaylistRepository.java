package ru.renett.repository.database;

import ru.renett.models.Playlist;
import ru.renett.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CRUDRepository<Playlist> {
    List<Playlist> getPlaylistsByOwnerId(String ownerId);
    Optional<Playlist> getPlaylistByYoutubeId(String youtubeId);
    void saveNewPlaylistVideo(String playlistId, String videoId);
}
