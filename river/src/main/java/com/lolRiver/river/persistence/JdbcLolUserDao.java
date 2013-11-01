package com.lolRiver.river.persistence;

import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.persistence.interfaces.LolUserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

@Repository
public class JdbcLolUserDao implements LolUserDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcLolUserDao.class);

    private static String GET_GIVEN_ID_SQL = "SELECT * FROM lol_users WHERE id = :id";

    private static String GET_ALL_SQL = "SELECT * FROM lol_users";

    private static String GET_GIVEN_STREAMER_SQL = "SELECT * FROM lol_users WHERE streamer_name = :name";

    private static String INSERT_SQL = "INSERT INTO lol_users (id, name, region, streamer_name) VALUES (:id, :username, :region, :streamerName)";

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private LolUserRowMapper rowMapper = new LolUserRowMapper();

    @Autowired
    public void init(final DataSource dataSource) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private LolUser queryForLolUser(final String sql, final LolUser ap,
                                    final RowMapper<LolUser> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(ap), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public LolUser getLolUserFromId(int id) {
        final LolUser queryObject = new LolUser();
        queryObject.setId(id);

        return queryForLolUser(GET_GIVEN_ID_SQL, queryObject, rowMapper);
    }

    @Override
    public List<LolUser> getLolUsers() {
        return jdbcTemplate.query(GET_ALL_SQL, rowMapper);
    }

    @Override
    public List<LolUser> getLolUsersFromStreamer(Streamer streamer) {
        return namedParamJdbcTemplate.query(GET_GIVEN_STREAMER_SQL, new BeanPropertySqlParameterSource(streamer), rowMapper);
    }

    @Override
    public LolUser insertLolUser(final LolUser lolUser) {
        LOGGER.debug("Creating LolUser with values: " + lolUser + " using query: " + INSERT_SQL);

        if (lolUser == null) {
            return null;
        }

        try {
            namedParamJdbcTemplate.update(INSERT_SQL, new BeanPropertySqlParameterSource(lolUser));
        } catch (DuplicateKeyException e) {
            // ignore
        }
        return getLolUserFromId(lolUser.getId());
    }

    // Extract LolUser results from a JDBC result set
    public final class LolUserRowMapper implements RowMapper<LolUser> {
        @Override
        public LolUser mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final int id = resultSet.getInt(LolUser.ID_STRING);
            final String username = resultSet.getString(LolUser.USERNAME_STRING);
            final String region = resultSet.getString(LolUser.REGION_STRING);
            final String streamerName = resultSet.getString(LolUser.STREAMER_NAME_STRING);

            return new LolUser()
                   .setId(id)
                   .setUserName(username)
                   .setRegion(region)
                   .setStreamerName(streamerName);
        }
    }
}
