package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.BlogStatistics;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class BlogStatisticsRepositoryImpl implements BlogStatisticsRepository{
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM statistics ORDER BY id";
    //language=sql
    private static final String SQL_FIND_BY_ID = "SELECT * FROM statistics WHERE id = ?";
    //language=sql
    private static final String SQL_FIND_BY_OWNER_ID = "SELECT * FROM statistics WHERE owner_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO statistics(owner_id, view_count, subscriber_count, video_count) VALUES (?, ?, ?, ?);";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE statistics set owner_id = ?, view_count = ?, subscriber_count = ?, video_count = ?  WHERE id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM statistics WHERE id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public BlogStatisticsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String id = "id";
    private static final String ownerId = "owner_id";
    private static final String viewCount = "view_count";
    private static final String subscriberCount = "subscriber_count";
    private static final String videoCount = "video_count";

    private final RowMapper<BlogStatistics> statisticsRowMapper = (row, rowNum) ->
            BlogStatistics.builder()
                    .id(row.getLong(id))
                    .ownerId(row.getString(ownerId))
                    .viewCount(row.getLong(viewCount))
                    .subscriberCount(row.getLong(subscriberCount))
                    .videoCount(row.getLong(videoCount))
                    .build();

    @Override
    public void save(BlogStatistics entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getOwnerId());
            preparedStatement.setLong(j++, entity.getViewCount());
            preparedStatement.setLong(j++, entity.getSubscriberCount());
            preparedStatement.setLong(j++, entity.getVideoCount());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving BlogStatistics", ex);
        }
    }

    @Override
    public void update(BlogStatistics entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getOwnerId(), entity.getViewCount(),
                    entity.getSubscriberCount(), entity.getVideoCount(),
                    entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating BlogStatistics", ex);
        }
    }

    @Override
    public void delete(BlogStatistics entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting BlogStatistics", ex);
        }
    }

    @Override
    public List<BlogStatistics> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, statisticsRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all BlogStatistics entities", ex);
        }
    }

    @Override
    public Optional<BlogStatistics> getBlogStatisticsById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_ID, statisticsRowMapper, id));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving BlogStatistics by id=" + id, ex);
        }
    }

    @Override
    public Optional<BlogStatistics> getBlogStatisticsByOwnerId(String ownerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_OWNER_ID, statisticsRowMapper, ownerId));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving BlogStatistics by ownerId=" + id, ex);
        }
    }
}
