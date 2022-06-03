package ru.renett.repository.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Company;
import ru.renett.models.Link;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static ru.renett.repository.database.LinkRepositoryImpl.linkRowMapper;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    //language=sql
    private static final String SQL_SELECT_ALL = "SELECT * FROM company ORDER BY id";
    //language=sql
    private static final String SQL_FIND_BY_TITLE = "SELECT * FROM company WHERE title = ?;";
    //language=sql
    private static final String SQL_INSERT = "INSERT INTO company(title, logo_url, domain) VALUES (?, ?, ?);";
    //language=sql
    private static final String SQL_FIND_BY_ID = "SELECT * FROM company WHERE id = ?";
    //language=sql
    private static final String SQL_UPDATE_BY_ID = "UPDATE company set title = ?, logo_url = ?, domain = ? WHERE id = ?;";
    //language=sql
    private static final String SQL_DELETE_BY_ID = "DELETE FROM company WHERE id = ?;";

    //language=sql
    private static final String SQL_GET_COMPANIES_BY_VIDEO_ID = "SELECT * FROM company left join videos_companies vc on company.id = vc.company_id WHERE vc.video_id = ?;";
    //language=sql
    private static final String SQL_SAVE_COMPANY_LINK = "INSERT INTO videos_companies(video_id, company_id, link_id) VALUES (?, ?, ?);";

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public CompanyRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    private static final String id = "id";
    private static final String title = "title";
    private static final String logoUrl = "logo_url";
    private static final String domain = "domain";

    private final RowMapper<Company> companyRowMapper = (row, rowNum) ->
            Company.builder()
                    .id(row.getLong(id))
                    .title(row.getString(title))
                    .logoUrl(row.getString(logoUrl))
                    .domain(row.getString(domain))
                    .build();

    @Override
    public void save(Company entity) throws DataBaseException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            int j = 1;
            preparedStatement.setString(j++, entity.getTitle());
            preparedStatement.setString(j++, entity.getLogoUrl());
            preparedStatement.setString(j++, entity.getDomain());

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                Long id = resultSet.getLong(1);
                entity.setId(id);
            }
        } catch (SQLException ex) {
            throw new DataBaseException("Problem with saving company entity", ex);
        }
    }

    @Override
    public void update(Company entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_UPDATE_BY_ID,
                    entity.getTitle(), entity.getTitle(), entity.getDomain(),
                    entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating company", ex);
        }
    }

    @Override
    public void delete(Company entity) throws DataBaseException {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_ID, entity.getId());
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with deleting company", ex);
        }
    }

    @Override
    public List<Company> findAll() throws DataBaseException {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL, companyRowMapper);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all companies", ex);
        }
    }

    @Override
    public Optional<Company> getCompanyByTitle(String title) {
        try {
            List<Company> userList = jdbcTemplate.query(SQL_FIND_BY_TITLE, companyRowMapper, title);

            if (userList.size() == 0) {
                return Optional.empty();
            } else {
                return Optional.of(userList.get(0));
            }
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving company by parameter '" + title + "'", ex);
        }
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_ID, companyRowMapper, id));
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving company by id = " + id, ex);
        }
    }

    @Override
    public void saveCompanyVideoLink(String videoId, Long companyId, Long linkId) {
        try {
            jdbcTemplate.update(SQL_SAVE_COMPANY_LINK,
                    videoId, companyId,
                    linkId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with updating company", ex);
        }
    }

    @Override
    public List<Company> getCompaniesByVideoId(String videoId) {
        try {
            return jdbcTemplate.query(SQL_GET_COMPANIES_BY_VIDEO_ID, companyRowMapper, videoId);
        } catch (DataAccessException ex) {
            throw new DataBaseException("Problem with retrieving all companies", ex);
        }
    }

}
