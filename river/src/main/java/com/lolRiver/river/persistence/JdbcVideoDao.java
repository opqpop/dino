package com.lolRiver.river.persistence;

import com.lolRiver.river.models.State;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.persistence.interfaces.VideoDao;
import com.lolRiver.river.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

@Repository
public class JdbcVideoDao implements VideoDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcVideoDao.class);

    private static String GET_GIVEN_ID_SQL = "SELECT * FROM videos WHERE id = :id";

    private static String GET_QUEUED_SQL = "SELECT * " +
                                           "FROM videos " +
                                           "WHERE state = 'QUEUED' " +
                                           "ORDER BY queued_at ASC";

    private static String INSERT_SQL = "INSERT INTO videos (streamer_name, url, views, start_time, end_time, length) VALUES (?,?,?,?,?,?)";

    private static String UPDATE_NONQUEUED_STATE_SQL = "UPDATE videos " +
                                                       "SET state = ? " +
                                                       "WHERE id = ?";

    private static String UPDATE_QUEUED_STATE_SQL = "UPDATE videos " +
                                                    "SET state = ?" +
                                                    ", queued_at = ?" +
                                                    "WHERE id = ?";

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private VideoRowMapper rowMapper = new VideoRowMapper();

    @Autowired
    public void init(final DataSource dataSource) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private Video queryForVideo(final String sql, final Video ap,
                                final RowMapper<Video> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(ap), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public Video getVideoFromId(int id) {
        final Video queryObject = new Video();
        queryObject.setId(id);

        return queryForVideo(GET_GIVEN_ID_SQL, queryObject, rowMapper);
    }

    @Override
    public List<Video> getUncovertedVideos() {
        return jdbcTemplate.query(GET_QUEUED_SQL, rowMapper);
    }

    @Override
    public Video insertVideo(final Video video) {
        LOGGER.debug("Creating Video with values: " + video + " using query: " + INSERT_SQL);

        if (video == null) {
            return null;
        }

        final KeyHolder holder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, video.getStreamerName());
                    ps.setString(2, video.getUrl());
                    ps.setInt(3, video.getViews());
                    ps.setTimestamp(4, video.getStartTime());
                    ps.setTimestamp(5, video.getEndTime());
                    ps.setInt(6, video.getLength());
                    return ps;
                }
            }, holder);
        } catch (DuplicateKeyException e) {
            // ignore
            return null;
        }
        return getVideoFromId(holder.getKey().intValue());
    }

    @Override
    public boolean updateVideoState(final int id, final State state) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(id));
        map.put("state", state.name());

        final Object[] params;
        String queryString;
        if (state == State.QUEUED) {
            params = new Object[]{state.name(), DateUtil.getCurrentTimestamp(), String.valueOf(id)};
            queryString = UPDATE_QUEUED_STATE_SQL;
        } else {
            params = new Object[]{state.name(), String.valueOf(id)};
            queryString = UPDATE_NONQUEUED_STATE_SQL;
        }

        try {
            jdbcTemplate.update(queryString, params);
        } catch (final DataAccessException e) {
            LOGGER.error("couldn't update video: " + id + " to state: " + state, e);
            return false;
        }
        return true;
    }

    // Extract Video results from a JDBC result set
    public final class VideoRowMapper implements RowMapper<Video> {
        @Override
        public Video mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final int id = resultSet.getInt(Video.ID_STRING);
            final String streamerName = resultSet.getString(Video.STREAMER_NAME_STRING);
            final String url = resultSet.getString(Video.URL_STRING);
            final Timestamp startTime = resultSet.getTimestamp(Video.START_TIME_STRING);
            final Timestamp endTime = resultSet.getTimestamp(Video.END_TIME_STRING);
            final int length = resultSet.getInt(Video.LENGTH_STRING);
            final int views = resultSet.getInt(Video.VIEWS_STRING);

            return new Video()
                   .setId(id)
                   .setStreamerName(streamerName)
                   .setUrl(url)
                   .setStartTime(startTime)
                   .setEndTime(endTime)
                   .setLength(length)
                   .setViews(views);
        }
    }
}
