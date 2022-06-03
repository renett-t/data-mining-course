package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Channel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class ChannelRepositoryImpl implements ChannelRepository {
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM channel";
    //language=sql
    private static final String SQL_FIND_BY_USERNAME = "SELECT * FROM channel WHERE username = ?;";
    //language=sql
    private static final String SQL_FIND_BY_YOUTUBE_ID = "SELECT * FROM channel WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO channel(youtube_id, username, description, thumbnail, uploads_id, given_reference) VALUES (?, ?, ?, ?, ?, ?);";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE channel set username = ?, description = ?, thumbnail = ?, uploads_id = ?, given_reference = ? WHERE youtube_id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM channel WHERE youtube_id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public ChannelRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String youtubeId = "youtube_id";
    private static final String userName = "username";
    private static final String description = "description";
    private static final String thumbnailUrl = "thumbnail";
    private static final String uploadsPlaylistId = "uploads_id";
    private static final String givenURI = "given_reference";

    private final RowMapper<Channel> channelRowMapper = (row, rowNum) ->
            Channel.builder()
                    .youtubeId(row.getString(youtubeId))
                    .userName(row.getString(userName))
                    .description(row.getString(description))
                    .thumbnailUrl(row.getString(thumbnailUrl))
                    .uploadsPlaylistId(row.getString(uploadsPlaylistId))
                    .givenURI(row.getString(givenURI))
                    .build();


    @Override
    public void save(Channel entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getYoutubeId());
            preparedStatement.setString(j++, entity.getUserName());
            preparedStatement.setString(j++, entity.getDescription());
            preparedStatement.setString(j++, entity.getThumbnailUrl());
            preparedStatement.setString(j++, entity.getUploadsPlaylistId());
            preparedStatement.setString(j++, entity.getGivenURI());

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving channel", ex);
        }
    }

    @Override
    public void update(Channel entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getUserName(), entity.getDescription(),
                    entity.getThumbnailUrl(), entity.getUploadsPlaylistId(), entity.getGivenURI(),
                    entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating channel", ex);
        }
    }

    @Override
    public void delete(Channel entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getYoutubeId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting channel", ex);
        }
    }

    @Override
    public List<Channel> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, channelRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all channels", ex);
        }
    }

    @Override
    public Optional<Channel> getChannelByYoutubeId(String youtubeId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_YOUTUBE_ID, channelRowMapper, youtubeId));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving Channel by id=" + youtubeId, ex);
        }
    }

    @Override
    public Optional<Channel> getChannelByUserName(String username) {
        return getChannel(username, SQL_FIND_BY_USERNAME);
    }

    private Optional<Channel> getChannel(String searchParam, String sql) {
        try {
            List<Channel> userList = jdbcTemplate.query(sql, channelRowMapper, searchParam);

            if (userList.size() == 0) {
                return Optional.empty();
            } else {
                return Optional.of(userList.get(0));
            }
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving channel by parameter '" + searchParam + "'", ex);
        }
    }
}
