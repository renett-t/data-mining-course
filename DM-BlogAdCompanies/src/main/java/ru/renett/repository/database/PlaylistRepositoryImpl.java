package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Playlist;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class PlaylistRepositoryImpl implements PlaylistRepository {
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM playlist";
    //language=sql
    private static final String SQL_FIND_BY_OWNER_ID = "SELECT * FROM playlist WHERE owner_id = ?;";
    //language=sql
    private static final String SQL_FIND_BY_YOUTUBE_ID = "SELECT * FROM playlist WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO playlist(youtube_id, owner_id) VALUES (?, ?);";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE playlist set owner_id = ? WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM playlist WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_INSERT_PLAYLIST_VIDEO = "INSERT INTO playlist_videos(playlist_id, video_id) VALUES (?, ?);";


    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public PlaylistRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String ownerId = "owner_id";
    private static final String youtubeId = "youtube_id";

    private final RowMapper<Playlist> playlistRowMapper = (row, rowNum) ->
            Playlist.builder()
                    .ownerId(row.getString(ownerId))
                    .youtubeId(row.getString(youtubeId))
                    .build();


    @Override
    public void save(Playlist entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getYoutubeId());
            preparedStatement.setString(j++, entity.getOwnerId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving Playlist", ex);
        }
    }

    @Override
    public void update(Playlist entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getOwnerId(),
                    entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating Playlist", ex);
        }
    }

    @Override
    public void delete(Playlist entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting Playlist", ex);
        }
    }

    @Override
    public List<Playlist> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, playlistRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Playlists", ex);
        }
    }

    @Override
    public List<Playlist> getPlaylistsByOwnerId(String ownerId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_OWNER_ID, playlistRowMapper, ownerId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Playlists", ex);
        }
    }

    @Override
    public Optional<Playlist> getPlaylistByYoutubeId(String youtubeId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_YOUTUBE_ID, playlistRowMapper, youtubeId));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving Playlist by id=" + youtubeId, ex);
        }
    }

    @Override
    public void saveNewPlaylistVideo(String playlistId, String videoId) {
        try {
            jdbcTemplate.update(SQL_INSERT_PLAYLIST_VIDEO,
                    playlistId,
                    videoId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating Playlist", ex);
        }
    }
}
