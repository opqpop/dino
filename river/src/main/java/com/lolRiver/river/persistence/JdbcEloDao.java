package com.lolRiver.river.persistence;

import com.lolRiver.river.models.Elo;
import com.lolRiver.river.persistence.interfaces.EloDao;
import com.lolRiver.river.util.sql.SqlQueryUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/1/13
 */

@Repository
public class JdbcEloDao implements EloDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcEloDao.class);

    private static String GET_GIVEN_ID_SQL = "SELECT * FROM elo WHERE id = :id";

    private static String INSERT_SQL = "INSERT INTO elo (user_id, elo, time) VALUES (?,?,?)";

    private static String GET_ELO_MATCHING_ELO_HEAD = "SELECT * " +
                                                      "FROM elo " +
                                                      "WHERE ";

    private static String GET_ELO_MATCHING_ELO_TAIL = " ORDER BY time DESC LIMIT 1";

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private EloRowMapper rowMapper = new EloRowMapper();

    @Autowired
    public void init(final DataSource dataSource) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private Elo queryForElo(final String sql, final Elo ap,
                            final RowMapper<Elo> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(ap), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public Elo getEloFromId(int id) {
        final Elo queryObject = new Elo();
        queryObject.setId(id);

        return queryForElo(GET_GIVEN_ID_SQL, queryObject, rowMapper);
    }

    @Override
    public Elo insertElo(final Elo elo) {
        LOGGER.debug("Creating Elo with values: " + elo + " using query: " + INSERT_SQL);

        if (elo == null) {
            return null;
        }

        final KeyHolder holder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, elo.getUserId());
                    ps.setString(2, elo.getName().name());
                    ps.setTimestamp(3, elo.getTime());
                    return ps;
                }
            }, holder);
        } catch (DuplicateKeyException e) {
            // ignore
            return null;
        }
        return getEloFromId(holder.getKey().intValue());
    }

    @Override
    public Elo getLatestEloFromElo(Elo elo, Map<String, String> paramsToMatch) {
        if (elo == null || paramsToMatch == null || paramsToMatch.isEmpty()) {
            LOGGER.warn("Tried to fetch elo with bad inputs. Elo: " + elo
                        + ", paramsToMatch: " + paramsToMatch);
            return null;
        }

        final String queryString = GET_ELO_MATCHING_ELO_HEAD + SqlQueryUtil.conditionalQuery(paramsToMatch) + GET_ELO_MATCHING_ELO_TAIL;
        LOGGER.info("Fetching elo: " + elo + " with: " + queryString);

        return namedParamJdbcTemplate.queryForObject(queryString, new BeanPropertySqlParameterSource(elo), rowMapper);
    }

    // Extract Elo results from a JDBC result set
    public final class EloRowMapper implements RowMapper<Elo> {
        @Override
        public Elo mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final int id = resultSet.getInt(Elo.ID_STRING);
            final int userId = resultSet.getInt(Elo.USER_ID_STRING);
            final Timestamp time = resultSet.getTimestamp(Elo.TIME_STRING);
            final String eloName = resultSet.getString(Elo.NAME_STRING);

            return Elo.fromString(eloName)
                   .setId(id)
                   .setUserId(userId)
                   .setTime(time);
        }
    }
}
