package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Video;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepositoryImpl implements VideoRepository{
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM video";
    //language=sql
    private static final String SQL_FIND_BY_OWNER_ID = "SELECT * FROM video WHERE owner_id = ?;";
    //language=sql
    private static final String SQL_FIND_BY_YOUTUBE_ID = "SELECT * FROM video WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_FIND_BY_PLAYLIST_ID = "SELECT * FROM playlist_videos pv LEFT JOIN video v " +
            "on v.youtube_id = pv.video_id WHERE pv.playlist_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO video(youtube_id, title, description, owner_id, thumbnail, view_count, like_count, comment_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE video set title = ?, description = ?, owner_id = ?, thumbnail = ?, view_count = ?, like_count = ?, comment_count = ? WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM video WHERE youtube_id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public VideoRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String youtubeId = "youtube_id";
    private static final String title = "title";
    private static final String description = "description";
    private static final String ownerId = "owner_id";
    private static final String thumbnailUrl = "thumbnail";
    private static final String viewCount = "view_count";
    private static final String likeCount = "like_count";
    private static final String commentCount = "comment_count";

    private final RowMapper<Video> videoRowMapper = (row, rowNum) -> {
        return Video.builder()
                .youtubeId(row.getString(youtubeId))
                .title(row.getString(title))
                .description(row.getString(description))
                .ownerId(row.getString(ownerId))
                .thumbnailUrl(row.getString(thumbnailUrl))
                .viewCount(row.getLong(viewCount))
                .likeCount(row.getLong(likeCount))
                .commentCount(row.getLong(commentCount))
                .build();
    };

    @Override
    public void save(Video entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getYoutubeId());
            preparedStatement.setString(j++, entity.getTitle());
            preparedStatement.setString(j++, entity.getDescription());
            preparedStatement.setString(j++, entity.getOwnerId());
            preparedStatement.setString(j++, entity.getThumbnailUrl());
            preparedStatement.setLong(j++, entity.getViewCount());
            preparedStatement.setLong(j++, entity.getLikeCount());
            preparedStatement.setLong(j++, entity.getCommentCount());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving Video", ex);
        }
    }

    @Override
    public void update(Video entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getTitle(), entity.getDescription(),
                    entity.getOwnerId(), entity.getThumbnailUrl(), entity.getViewCount(),
                    entity.getLikeCount(), entity.getCommentCount(),
                    entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating Video", ex);
        }
    }

    @Override
    public void delete(Video entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting Video", ex);
        }
    }

    @Override
    public List<Video> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, videoRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Videos", ex);
        }
    }

    @Override
    public Optional<Video> getVideoByYoutubeId(String youtubeId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_YOUTUBE_ID, videoRowMapper, youtubeId));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving Playlist by id=" + youtubeId, ex);
        }
    }

    @Override
    public List<Video> getVideosByPlaylistId(String playlistId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_PLAYLIST_ID, videoRowMapper, playlistId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Playlists", ex);
        }
    }

    @Override
    public List<Video> getVideosByOwnerId(String ownerId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_OWNER_ID, videoRowMapper, ownerId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Playlists", ex);
        }
    }
}
