package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.BlackListedLink;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BlackListedLinkRepositoryImpl implements BlackListedLinkRepository{
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM blacklisted_link ORDER BY id";
    //language=sql
    private static final String SQL_FIND_BY_ID = "SELECT * FROM blacklisted_link WHERE id = ?";
    //language=sql
    private static final String SQL_FIND_BY_CHANNEL_ID = "SELECT * FROM blacklisted_link WHERE channel_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO blacklisted_link(value, channel_id) VALUES (?, ?);";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE blacklisted_link set value = ?, channel_id = ? WHERE id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM blacklisted_link WHERE id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public BlackListedLinkRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String id = "id";
    private static final String channelId = "channel_id";
    private static final String value = "value";

    private final RowMapper<BlackListedLink> linkRowMapper = (row, rowNum) ->
            BlackListedLink.builder()
                    .id(row.getLong(id))
                    .value(row.getString(value))
                    .channelId(row.getString(channelId))
                    .build();


    @Override
    public void save(BlackListedLink entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getValue());
            preparedStatement.setString(j++, entity.getChannelId());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving BlackListedLink", ex);
        }
    }

    @Override
    public void update(BlackListedLink entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getValue(), entity.getChannelId(),
                    entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating BlackListedLink", ex);
        }
    }

    @Override
    public void delete(BlackListedLink entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting BlackListedLink", ex);
        }
    }

    @Override
    public List<BlackListedLink> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, linkRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Videos", ex);
        }
    }

    @Override
    public Optional<BlackListedLink> getBlackListedLinkById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_ID, linkRowMapper, id));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving Video by id=" + id, ex);
        }
    }

    @Override
    public List<BlackListedLink> getLinksBlackListedForChannel(String channelId) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_CHANNEL_ID, linkRowMapper, channelId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all Videos", ex);
        }
    }
}
