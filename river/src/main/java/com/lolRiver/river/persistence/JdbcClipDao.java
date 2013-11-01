package com.lolRiver.river.persistence;

import com.lolRiver.river.models.*;
import com.lolRiver.river.persistence.interfaces.ClipDao;
import com.lolRiver.river.util.DateUtil;
import com.lolRiver.river.util.StringUtil;
import com.lolRiver.river.util.sql.SqlQueryUtil;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         9/30/13
 */

@Repository
public class JdbcClipDao implements ClipDao {
    private static final Logger LOGGER = Logger.getLogger(JdbcClipDao.class);

    private static String GET_TOTAL_COUNT_SQL = "SELECT count(*) FROM clips";

    private static String GET_GIVEN_ID_SQL = "SELECT * FROM clips WHERE id = :id";

    private static String GET_CLIPS_HEAD_SQL = "SELECT * FROM clips";

    private static String INSERT_SQL = "INSERT INTO clips (video_id, game_id, streamer_name, start_time, end_time, length, views, url, champion_played, role_played, champion_faced, lane_partner_champion, enemy_lane_partner_champion, elo, game_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private ClipRowMapper rowMapper = new ClipRowMapper();

    @Autowired
    public void init(final DataSource dataSource) {
        namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // helper method to handle results with num rows != 1
    private Clip queryForClip(final String sql, final Clip ap,
                              final RowMapper<Clip> rowMapper) {
        try {
            return namedParamJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(ap), rowMapper);
        } catch (final EmptyResultDataAccessException e) {
            // not an error when no rows were found
            return null;
        }
    }

    @Override
    public int getNumTotalClips() {
        return jdbcTemplate.queryForInt(GET_TOTAL_COUNT_SQL);
    }

    @Override
    public int getNumTotalClips(String orderBy, boolean descending, Clip clip) {
        String query = constructSelectQueryFromClip(null, null, orderBy, descending, clip);
        query = query.replace("*", "count(*)");
        return jdbcTemplate.queryForInt(query);
    }

    @Override
    public Clip getClipFromId(int id) {
        final Clip queryObject = new Clip();
        queryObject.setId(id);

        return queryForClip(GET_GIVEN_ID_SQL, queryObject, rowMapper);
    }

    @Override
    public List<Clip> getClips(int offset, int size, String orderBy, boolean descending) {
        String query;
        if (descending) {
            query = String.format(GET_CLIPS_HEAD_SQL + " ORDER BY %s DESC LIMIT %d, %d", orderBy, offset,
                                 size);
        } else {
            query = String.format(GET_CLIPS_HEAD_SQL + " ORDER BY %s ASC LIMIT %d, %d", orderBy, offset,
                                 size);
        }
        LOGGER.debug("QUERYING DEFAULT CLIPS FROM FRONT-END QUERY: " + query);
        return jdbcTemplate.query(query, rowMapper);
    }

    private String constructSelectQueryFromClip(Integer offset, Integer size, String orderBy, boolean descending,
                                                Clip clip) {
        String query = GET_CLIPS_HEAD_SQL;

        StringBuilder sb = new StringBuilder(" WHERE ");
        boolean isFirstCondition = true;
        if (!StringUtils.isBlank(clip.getStreamerName())) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.STREAMER_NAME_STRING + " = " +
                      StringUtil.addSurroundingQuotes(clip.getStreamerName().toLowerCase()));
        }
        if (!StringUtils.isBlank(clip.getChampionPlayedString())) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.CHAMPION_PLAYED_STRING + " = " +
                      StringUtil.addSurroundingQuotes(Champion.Name.normalizedName(clip.getChampionPlayedString())));
        }
        if (!StringUtils.isBlank(clip.getChampionFacedString())) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.CHAMPION_FACED_STRING + " = " +
                      StringUtil.addSurroundingQuotes(clip.getChampionFacedString()));
        }
        if (!StringUtils.isBlank(clip.getMinLength())) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.LENGTH_STRING + " >= " + clip.getMinLength());
        }
        if (!StringUtils.isBlank(clip.getMaxLength())) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.LENGTH_STRING + " <= " + clip.getMaxLength());
        }
        List<String> roleCriteria = clip.getRoleCriteria();
        if (roleCriteria != null && roleCriteria.size() > 0) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            sb.append(Clip.ROLE_PLAYED_STRING + " IN " + SqlQueryUtil.inClause(roleCriteria, true));
        }
        List<String> eloCriteria = clip.getEloCriteria();
        if (eloCriteria != null && eloCriteria.size() > 0) {
            if (isFirstCondition) {
                isFirstCondition = false;
            } else {
                sb.append(" AND ");
            }
            List<String> dbSpecificEloCriteria = new ArrayList<String>();
            for (String generalElo : eloCriteria) {
                dbSpecificEloCriteria.addAll(Elo.dbEloFromGeneralElo(generalElo));
            }
            sb.append(Clip.ELO_STRING + " IN " + SqlQueryUtil.inClause(dbSpecificEloCriteria, true));
        }
        String conditionQuery = sb.toString();
        conditionQuery = conditionQuery.equals(" WHERE ") ? "" : conditionQuery;
        query += conditionQuery;

        if (descending) {
            query += String.format(" ORDER BY %s DESC", orderBy);
        } else {
            query += String.format(" ORDER BY %s ASC", orderBy);
        }

        if (offset != null && size != null) {
            query += String.format(" LIMIT %d, %d", offset, size);
        }
        return query;
    }

    @Override
    public List<Clip> getClipsFromClip(int offset, int size, String orderBy, boolean descending, Clip clip) {
        String query = constructSelectQueryFromClip(offset, size, orderBy, descending, clip);
        LOGGER.debug("QUERYING CLIPS FROM FRONT-END QUERY: " + query);
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public Clip insertClip(final Clip clip) {
        LOGGER.debug("Creating Clip with values: " + clip + " using query: " + INSERT_SQL);

        if (clip == null) {
            return null;
        }

        final KeyHolder holder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                    final PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, clip.getVideoId());
                    ps.setInt(2, clip.getGameId());
                    ps.setString(3, clip.getStreamerName());
                    ps.setTimestamp(4, clip.getStartTime());
                    ps.setTimestamp(5, clip.getEndTime());
                    ps.setInt(6, clip.getLength());
                    ps.setInt(7, clip.getViews());
                    ps.setString(8, clip.getUrl());

                    Champion championPlayed = clip.getChampionPlayed();
                    if (championPlayed == null) {
                        LOGGER.error("trying to insert clip with empty field championPlayed: " + clip);
                    }
                    ps.setString(9, championPlayed == null ? "" : championPlayed.getName().name());

                    Role rolePlayed = clip.getRolePlayed();
                    if (rolePlayed == null) {
                        LOGGER.error("trying to insert clip with empty field rolePlayed: " + clip);
                    }
                    ps.setString(10, rolePlayed == null ? "" : rolePlayed.getName().name());

                    Champion championFaced = clip.getChampionFaced();
                    if (championFaced == null) {
                        LOGGER.error("trying to insert clip with empty field championFaced: " + clip);
                    }
                    ps.setString(11, championFaced == null ? "" : championFaced.getName().name());

                    Champion lanePartnerChampion = clip.getLanePartnerChampion();
                    ps.setString(12, lanePartnerChampion == null ? "" : lanePartnerChampion.getName().name());

                    Champion enemyLanePartnerChampion = clip.getEnemyLanePartnerChampion();
                    ps.setString(13, enemyLanePartnerChampion == null ? "" : enemyLanePartnerChampion.getName().name());

                    Elo elo = clip.getElo();
                    if (elo == null) {
                        LOGGER.error("trying to insert clip with empty field elo: " + clip);
                    }
                    ps.setString(14, elo == null ? "" : elo.getName().name());

                    Game.Type gameType = clip.getGameType();
                    if (gameType == null) {
                        LOGGER.error("trying to insert clip with empty field gameType: " + clip);
                    }
                    ps.setString(15, gameType == null ? "" : gameType.name());
                    return ps;
                }
            }, holder);
        } catch (DuplicateKeyException e) {
            // ignore
            return null;
        }
        return getClipFromId(holder.getKey().intValue());
    }

    // Extract Clip results from a JDBC result set
    public final class ClipRowMapper implements RowMapper<Clip> {
        @Override
        public Clip mapRow(final ResultSet resultSet, final int i) throws SQLException {
            final int id = resultSet.getInt(Clip.ID_STRING);
            final int videoId = resultSet.getInt(Clip.VIDEO_ID_STRING);
            final int gameId = resultSet.getInt(Clip.GAME_ID_STRING);
            final String streamerName = resultSet.getString(Clip.STREAMER_NAME_STRING);
            final Timestamp startTime = resultSet.getTimestamp(Clip.START_TIME_STRING);
            final Timestamp endTime = resultSet.getTimestamp(Clip.END_TIME_STRING);
            final int length = resultSet.getInt(Clip.LENGTH_STRING);
            final int views = resultSet.getInt(Clip.VIEWS_STRING);
            final String gameType = resultSet.getString(Clip.GAME_TYPE_STRING);
            final String url = resultSet.getString(Clip.URL_STRING);
            final String championPlayed = resultSet.getString(Clip.CHAMPION_PLAYED_STRING);
            final String rolePlayed = resultSet.getString(Clip.ROLE_PLAYED_STRING);
            final String championFaced = resultSet.getString(Clip.CHAMPION_FACED_STRING);
            final String lanePartnerChampion = resultSet.getString(Clip.LANE_PARTNER_CHAMPION_STRING);
            final String enemyLanePartnerChampion = resultSet.getString(Clip.ENEMY_LANE_PARTNER_CHAMPION_STRING);
            final String elo = resultSet.getString(Clip.ELO_STRING);

            // non-db variables
            final String generalElo = Elo.generalEloFromElo(Elo.fromString(elo));
            final String timeSinceNowMessage = DateUtil.timeSinceNowMessage(startTime);

            return new Clip()
                   .setId(id)
                   .setVideoId(videoId)
                   .setGameId(gameId)
                   .setStreamerName(streamerName)
                   .setStartTime(startTime)
                   .setEndTime(endTime)
                   .setLength(length)
                   .setViews(views)
                   .setUrl(url)
                   .setGameType(Game.gameTypeFromString(gameType))
                   .setChampionPlayed(Champion.fromString(championPlayed))
                   .setRolePlayed(Role.fromString(rolePlayed))
                   .setChampionFaced(Champion.fromString(championFaced))
                   .setLanePartnerChampion(Champion.fromString(lanePartnerChampion))
                   .setEnemyLanePartnerChampion(Champion.fromString(enemyLanePartnerChampion))
                   .setElo(Elo.fromString(elo))
                   .setGeneralElo(generalElo)
                   .setTimeSinceNowMessage(timeSinceNowMessage);
        }
    }
}
