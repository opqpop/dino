package com.lolRiver.river.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

public class Clip {
    public static final String ID_STRING = "id";
    public static final String GAME_ID_STRING = "game_id";
    public static final String VIDEO_ID_STRING = "video_id";
    public static final String GAME_TYPE_STRING = "game_type";
    public static final String URL_STRING = "url";
    public static final String STREAMER_NAME_STRING = "streamer_name";
    public static final String START_TIME_STRING = "start_time";
    public static final String END_TIME_STRING = "end_time";
    public static final String LENGTH_STRING = "length";
    public static final String VIEWS_STRING = "views";
    public static final String CHAMPION_PLAYED_STRING = "champion_played";
    public static final String ROLE_PLAYED_STRING = "role_played";
    public static final String CHAMPION_FACED_STRING = "champion_faced";
    public static final String LANE_PARTNER_CHAMPION_STRING = "lane_partner_champion";
    public static final String ENEMY_LANE_PARTNER_CHAMPION_STRING = "enemy_lane_partner_champion";
    public static final String ELO_STRING = "elo";

    private int id;
    private int gameId;
    private int videoId;
    private Game.Type gameType;
    private String url;
    private String streamerName;
    private Timestamp startTime;
    private Timestamp endTime;
    private int length;             // video length in seconds
    private int views;
    private Champion championPlayed;
    private Role rolePlayed;
    private Champion championFaced;
    private Champion lanePartnerChampion;
    private Champion enemyLanePartnerChampion;
    private Elo elo;

    /* START - Non DB variables */
    private String timeSinceNowMessage;     // used for index.jsp
    private boolean isViewable; // set by video converter to know if clip is viewable

    // front-end related for searching clips
    private String generalElo;
    private String championPlayedString;
    private String championFacedString;
    private List<String> eloCriteria;
    private List<String> roleCriteria;
    private String minLength;
    private String maxLength;

    /* END - Non DB variables */

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    public int getVideoId() {
        return videoId;
    }

    public Game.Type getGameType() {
        return gameType;
    }

    public Clip setGameType(Game.Type gameType) {
        this.gameType = gameType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public int getLength() {
        return length;
    }

    public int getViews() {
        return views;
    }

    public Champion getChampionPlayed() {
        return championPlayed;
    }

    public Role getRolePlayed() {
        return rolePlayed;
    }

    public Champion getChampionFaced() {
        return championFaced;
    }

    public Champion getLanePartnerChampion() {
        return lanePartnerChampion;
    }

    public Champion getEnemyLanePartnerChampion() {
        return enemyLanePartnerChampion;
    }

    public Elo getElo() {
        return elo;
    }

    public Clip setId(int id) {
        this.id = id;
        return this;
    }

    public Clip setGameId(int gameId) {
        this.gameId = gameId;
        return this;
    }

    public Clip setVideoId(int videoId) {
        this.videoId = videoId;
        return this;
    }

    public Clip setUrl(String url) {
        this.url = url;
        return this;
    }

    public Clip setStreamerName(String streamerName) {
        this.streamerName = streamerName;
        return this;
    }

    public Clip setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        return this;
    }

    public Clip setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        return this;
    }

    public Clip setLength(int length) {
        this.length = length;
        return this;
    }

    public Clip setViews(int views) {
        this.views = views;
        return this;
    }

    public Clip setChampionPlayed(Champion championPlayed) {
        this.championPlayed = championPlayed;
        return this;
    }

    public Clip setRolePlayed(Role rolePlayed) {
        this.rolePlayed = rolePlayed;
        return this;
    }

    public Clip setChampionFaced(Champion championFaced) {
        this.championFaced = championFaced;
        return this;
    }

    public Clip setLanePartnerChampion(Champion lanePartnerChampion) {
        this.lanePartnerChampion = lanePartnerChampion;
        return this;
    }

    public Clip setEnemyLanePartnerChampion(Champion enemyLanePartnerChampion) {
        this.enemyLanePartnerChampion = enemyLanePartnerChampion;
        return this;
    }

    public Clip setElo(Elo elo) {
        this.elo = elo;
        return this;
    }

    public String getTimeSinceNowMessage() {
        return timeSinceNowMessage;
    }

    public Clip setTimeSinceNowMessage(String timeSinceNowMessage) {
        this.timeSinceNowMessage = timeSinceNowMessage;
        return this;
    }

    public boolean isViewable() {
        return isViewable;
    }

    public void setViewable(boolean viewable) {
        isViewable = viewable;
    }

    public String getGeneralElo() {
        return generalElo;
    }

    public Clip setGeneralElo(String generalElo) {
        this.generalElo = generalElo;
        return this;
    }

    public String getChampionPlayedString() {
        return championPlayedString;
    }

    public Clip setChampionPlayedString(String championPlayedString) {
        this.championPlayedString = championPlayedString;
        return this;
    }

    public String getChampionFacedString() {
        return championFacedString;
    }

    public Clip setChampionFacedString(String championFacedString) {
        this.championFacedString = championFacedString;
        return this;
    }

    public List<String> getRoleCriteria() {
        return roleCriteria;
    }

    public Clip setRoleCriteria(List<String> roleCriteria) {
        this.roleCriteria = new ArrayList<String>(roleCriteria);
        return this;
    }

    public List<String> getEloCriteria() {
        return eloCriteria;
    }

    public Clip setEloCriteria(List<String> eloCriteria) {
        this.eloCriteria = new ArrayList<String>(eloCriteria);
        return this;
    }

    public String getMinLength() {
        return minLength;
    }

    public Clip setMinLength(String minLength) {
        this.minLength = minLength;
        return this;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public Clip setMaxLength(String maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public String toString() {
        return "Clip{" +
               "id=" + id +
               ", gameId=" + gameId +
               ", videoId=" + videoId +
               ", gameType=" + gameType +
               ", url='" + url + '\'' +
               ", streamerName='" + streamerName + '\'' +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               ", length=" + length +
               ", views=" + views +
               ", championPlayed=" + championPlayed +
               ", rolePlayed=" + rolePlayed +
               ", championFaced=" + championFaced +
               ", lanePartnerChampion=" + lanePartnerChampion +
               ", enemyLanePartnerChampion=" + enemyLanePartnerChampion +
               ", elo=" + elo +
               ", timeSinceNowMessage='" + timeSinceNowMessage + '\'' +
               ", generalElo='" + generalElo + '\'' +
               ", championPlayedString='" + championPlayedString + '\'' +
               ", championFacedString='" + championFacedString + '\'' +
               ", eloCriteria=" + eloCriteria +
               ", roleCriteria=" + roleCriteria +
               ", minLength='" + minLength + '\'' +
               ", maxLength='" + maxLength + '\'' +
               '}';
    }
}
