package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Link;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class LinkRepositoryImpl implements LinkRepository{
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM link ORDER BY id";
    //language=sql
    private static final String SQL_FIND_BY_OWNER_ID = "SELECT * FROM link WHERE owner_id = ?;";
    //language=sql
    private static final String SQL_FIND_BY_VIDEO_ID = "SELECT * FROM link WHERE video_id = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO link(video_id, owner_id, raw_value) VALUES (?, ?, ?);";
    //language=sql
    private static final String SQL_FIND_BY_ID = "SELECT * FROM link WHERE id = ?";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE link set video_id = ?, owner_id = ?, raw_value = ?, is_blacklisted = ?, company_id = ?, value = ? WHERE id = ?;";
    //language=sql
    private static final String SQL_SET_BLACKLISTED = "UPDATE link set is_blacklisted = TRUE WHERE id = ?;";
    //language=sql
    private static final String SQL_SET_UPDATED_LINK = "UPDATE link set value = ? WHERE id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM link WHERE id = ?;";
    //language=sql
    private static final String SQL_GET_LINK_BY_COMPANY_ID = "SELECT * FROM\n" +
            "    videos_companies left join link l on l.id = videos_companies.link_id\n" +
            "WHERE videos_companies.company_id = ? AND videos_companies.video_id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public LinkRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String id = "id";
    private static final String videoId = "video_id";
    private static final String ownerId = "owner_id";
    private static final String value = "value";
    private static final String isBlacklisted = "is_blacklisted";
    private static final String companyId = "company_id";
    private static final String rawValue = "raw_value";


    public static final RowMapper<Link> linkRowMapper = (row, rowNum) ->
            Link.builder()
                    .id(row.getLong(id))
                    .videoId(row.getString(videoId))
                    .channelId(row.getString(ownerId))
                    .rawValue(row.getString(value))
                    .isBlackListed(row.getBoolean(isBlacklisted))
                    .companyId(row.getLong(companyId))
                    .rawValue(row.getString(rawValue))
                    .build();


    @Override
    public void save(Link entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getVideoId());
            preparedStatement.setString(j++, entity.getChannelId());
            preparedStatement.setString(j++, entity.getRawValue());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving link entity", ex);
        }
    }

    @Override
    public void update(Link entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getVideoId(), entity.getChannelId(), entity.getRawValue(),
                    entity.isBlackListed(), entity.getCompanyId(), entity.getValue(),
                    entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating link entity", ex);
        }
    }

    @Override
    public void delete(Link entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting Link entity", ex);
        }
    }

    @Override
    public List<Link> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, linkRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all links", ex);
        }
    }

    @Override
    public Optional<Link> getLinkById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_ID, linkRowMapper, id));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving Link by id=" + id, ex);
        }
    }

    @Override
    public List<Link> getLinksByOwnerId(String ownerId) {
        return getLinks(ownerId, SQL_FIND_BY_OWNER_ID);
    }

    @Override
    public List<Link> getLinksByVideoId(String videoId) {
        return getLinks(videoId, SQL_FIND_BY_VIDEO_ID);
    }

    @Override
    public void setBlackListedById(Long id) {
        try {
            jdbcTemplate.update(SQL_SET_BLACKLISTED, id);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting Link entity", ex);
        }
    }

    @Override
    public void setUpdatedLink(Long id, String link) {
        try {
            jdbcTemplate.update(SQL_SET_UPDATED_LINK, link, id);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating Link", ex);
        }
    }

    private List<Link> getLinks(String searchParam, String sql) {
        try {
            return jdbcTemplate.query(sql, linkRowMapper, searchParam);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all links", ex);
        }
    }

    @Override
    public Link getLinkByCompanyAndVideoId(Long id, String videoId) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_LINK_BY_COMPANY_ID, linkRowMapper, id, videoId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving company by id = " + id, ex);
        }
    }
}
