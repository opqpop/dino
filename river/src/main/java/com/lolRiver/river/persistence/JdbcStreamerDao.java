package com.lolRiver.river.persistence;

import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.persistence.interfaces.StreamerDao;
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
public class JdbcStreamerDao implements StreamerDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcStreamerDao.class);

    private static String GET_GIVEN_NAME_SQL = "SELECT * FROM streamers WHERE name = :name";
    private static String GET_ALL_SQL = "SELECT * FROM streamers";

    private static String INSERT_SQL = "INSERT INTO streamers (name) VALUES (:name)";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private StreamerRowMapper rowMapper = new StreamerRowMapper();

    @Autowired
    public void init(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private Streamer queryForStreamer(final String sql, final Streamer ap,
                                      final RowMapper<Streamer> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(ap), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public Streamer getStreamerFromName(String name) {
        final Streamer queryObject = new Streamer();
        queryObject.setName(name);

        return queryForStreamer(GET_GIVEN_NAME_SQL, queryObject, rowMapper);
    }

    @Override
    public List<Streamer> getStreamers() {
        return jdbcTemplate.query(GET_ALL_SQL, rowMapper);
    }

    @Override
    public Streamer insertStreamer(final Streamer streamer) {
        LOGGER.debug("Creating Streamer with values: " + streamer + " using query: " + INSERT_SQL);

        if (streamer == null) {
            return null;
        }

        try {
            namedParamJdbcTemplate.update(INSERT_SQL, new BeanPropertySqlParameterSource(streamer));
        } catch (DuplicateKeyException e) {
            // ignore
            return null;
        }
        return getStreamerFromName(streamer.getName());
    }

    // Extract Streamer results from a JDBC result set
    public final class StreamerRowMapper implements RowMapper<Streamer> {
        @Override
        public Streamer mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final String name = resultSet.getString(Streamer.NAME_STRING);
            return new Streamer()
                   .setName(name);
        }
    }
}
