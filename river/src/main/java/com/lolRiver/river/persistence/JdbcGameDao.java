package com.lolRiver.river.persistence;

import com.lolRiver.river.models.Game;
import com.lolRiver.river.models.LolUser;
import com.lolRiver.river.models.Streamer;
import com.lolRiver.river.models.Video;
import com.lolRiver.river.persistence.interfaces.GameDao;
import com.lolRiver.river.util.sql.SqlQueryUtil;
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
import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/29/13
 */

@Repository
public class JdbcGameDao implements GameDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcGameDao.class);

    private static String GET_GIVEN_ID_SQL = "SELECT * FROM games WHERE id = :id";

    // length > 0 means kassadin's recent games was merged in.
    private static String GET_LATEST_SQL = "SELECT * FROM games WHERE length > 0 ORDER BY start_time DESC LIMIT 1";

    private static String INSERT_SQL = "INSERT INTO games (id, type, blue_player1_id, blue_player2_id, blue_player3_id, blue_player4_id, blue_player5_id, red_player1_id, red_player2_id, red_player3_id, red_player4_id, red_player5_id, players_info, won, start_time, end_time, length) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static String UPDATE_SQL = "UPDATE games " +
                                       "SET won = ?" + ", length = ?" +
                                       ", end_time = ?" +
                                       " WHERE id = ?";

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private GameRowMapper rowMapper = new GameRowMapper();

    @Autowired
    private DaoCollection daoCollection;

    @Autowired
    public void init(final DataSource dataSource) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private Game queryForGame(final String sql, final Game game,
                              final RowMapper<Game> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(game), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public Game getGameFromId(int id) {
        final Game queryObject = new Game();
        queryObject.setId(id);

        return queryForGame(GET_GIVEN_ID_SQL, queryObject, rowMapper);
    }

    @Override
    public Game getLatestGame() {
        return jdbcTemplate.queryForObject(GET_LATEST_SQL, rowMapper);
    }

    @Override
    public List<Game> getGamesMatchingVideo(Video video) {
        String sqlQuery = "SELECT * FROM games WHERE start_time > ? AND end_time < ? AND ";

        List<LolUser> users = daoCollection.getLolUserDao().getLolUsersFromStreamer(new Streamer(video
                                                                                                 .getStreamerName()));
        if (users.isEmpty()) {
            LOGGER.error("no users found for streamer: " + video.getStreamerName() + " video: " + video);
            return new ArrayList<Game>();
        }

        List<String> userIds = new ArrayList<String>();
        for (LolUser user : users) {
            userIds.add(String.valueOf(user.getId()));
        }
        String inClause = SqlQueryUtil.inClause(userIds);

        sqlQuery += String.format("(blue_player1_id IN %s OR" +
                                  " blue_player2_id IN %s OR" +
                                  " blue_player3_id IN %s OR" +
                                  " blue_player4_id IN %s OR" +
                                  " blue_player5_id IN %s OR" +
                                  " red_player1_id IN %s OR" +
                                  " red_player2_id IN %s OR" +
                                  " red_player3_id IN %s OR" +
                                  " red_player4_id IN %s OR" +
                                  " red_player5_id IN %s)",
                                 inClause, inClause, inClause, inClause, inClause,
                                 inClause, inClause, inClause, inClause, inClause);

        final Object[] params = new Object[]{video.getStartTime(),
                                            video.getEndTime()};
        return jdbcTemplate.query(sqlQuery, params, rowMapper);
    }

    @Override
    public Game insertGame(final Game game) {
        LOGGER.debug("Creating Game with values: " + game + " using query: " + INSERT_SQL);

        if (game == null) {
            return null;
        }

        final KeyHolder holder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, game.getId());
                    ps.setString(2, game.getType());
                    ps.setInt(3, game.getBluePlayer1Id());
                    ps.setInt(4, game.getBluePlayer2Id());
                    ps.setInt(5, game.getBluePlayer3Id());
                    ps.setInt(6, game.getBluePlayer4Id());
                    ps.setInt(7, game.getBluePlayer5Id());
                    ps.setInt(8, game.getRedPlayer1Id());
                    ps.setInt(9, game.getRedPlayer2Id());
                    ps.setInt(10, game.getRedPlayer3Id());
                    ps.setInt(11, game.getRedPlayer4Id());
                    ps.setInt(12, game.getRedPlayer5Id());
                    ps.setString(13, game.getPlayersInfo());
                    ps.setBoolean(14, game.isWon());
                    ps.setTimestamp(15, game.getStartTime());
                    ps.setTimestamp(16, game.getEndTime());
                    ps.setInt(17, game.getLength());
                    return ps;
                }
            }, holder);
        } catch (DuplicateKeyException e) {
            // ignore
            return null;
        }
        return getGameFromId(holder.getKey().intValue());
    }

    @Override
    public boolean updateGame(Game game) {
        LOGGER.debug("Updating Game: " + game + " using query: " + UPDATE_SQL);

        if (game == null) {
            throw new IllegalArgumentException("Abacus problem must not be null");
        }

        final Object[] params = new Object[]{game.isWon(),
                                            game.getLength(),
                                            game.getEndTime(),
                                            game.getId()};
        try {
            jdbcTemplate.update(UPDATE_SQL, params);
        } catch (final DataAccessException e) {
            return false;
        }
        return true;
    }

    // Extract Game results from a JDBC result set
    public final class GameRowMapper implements RowMapper<Game> {
        @Override
        public Game mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final int id = resultSet.getInt(Game.ID_STRING);
            final String type = resultSet.getString(Game.TYPE_STRING);
            final boolean won = resultSet.getBoolean(Game.WON_STRING);
            final Timestamp startTime = resultSet.getTimestamp(Game.START_TIME_STRING);
            final Timestamp endTime = resultSet.getTimestamp(Game.END_TIME_STRING);
            final int length = resultSet.getInt(Game.LENGTH_STRING);
            final int bluePlayer1Id = resultSet.getInt(Game.BLUE_PLAYER1_ID_STRING);
            final int bluePlayer2Id = resultSet.getInt(Game.BLUE_PLAYER2_ID_STRING);
            final int bluePlayer3Id = resultSet.getInt(Game.BLUE_PLAYER3_ID_STRING);
            final int bluePlayer4Id = resultSet.getInt(Game.BLUE_PLAYER4_ID_STRING);
            final int bluePlayer5Id = resultSet.getInt(Game.BLUE_PLAYER5_ID_STRING);
            final int redPlayer1Id = resultSet.getInt(Game.RED_PLAYER1_ID_STRING);
            final int redPlayer2Id = resultSet.getInt(Game.RED_PLAYER2_ID_STRING);
            final int redPlayer3Id = resultSet.getInt(Game.RED_PLAYER3_ID_STRING);
            final int redPlayer4Id = resultSet.getInt(Game.RED_PLAYER4_ID_STRING);
            final int redPlayer5Id = resultSet.getInt(Game.RED_PLAYER5_ID_STRING);
            final String playersInfo = resultSet.getString(Game.PLAYERS_INFO_STRING);

            return new Game()
                   .setId(id)
                   .setType(type)
                   .setWon(won)
                   .setStartTime(startTime)
                   .setEndTime(endTime)
                   .setLength(length)
                   .setBluePlayer1Id(bluePlayer1Id)
                   .setBluePlayer2Id(bluePlayer2Id)
                   .setBluePlayer3Id(bluePlayer3Id)
                   .setBluePlayer4Id(bluePlayer4Id)
                   .setBluePlayer5Id(bluePlayer5Id)
                   .setRedPlayer1Id(redPlayer1Id)
                   .setRedPlayer2Id(redPlayer2Id)
                   .setRedPlayer3Id(redPlayer3Id)
                   .setRedPlayer4Id(redPlayer4Id)
                   .setRedPlayer5Id(redPlayer5Id)
                   .setPlayersInfo(playersInfo);
        }
    }
}
