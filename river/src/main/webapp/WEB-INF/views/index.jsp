<%--
  Created by IntelliJ IDEA.
  User: mxia
  Date: 10/8/13
  Time: 10:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>LolRiver - League of Legends Video Stream Portal</title>
    <meta name="keywords" content="League of Legends, LoL, video game, stream, twitch, streaming, LoL game, elo">
    <meta name="description"
          content="League of Legends Video Stream Portal - Browse and watch LoL streams based on champion played, streamer, elo, and more">
    <meta name="author" content="Mark Donkey">

    <link rel="stylesheet" type="text/css" href="/static/css/index.css"/>
    <link rel="stylesheet" href="/static/css/jquery/ui-lightness/jquery-ui-1.10.3.custom.css"/>
    <link rel="stylesheet" href="/static/css/jqpagination.css"/>

    <script src="/static/script/util.js"></script>
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script src="/static/script/jgPagination/jquery.jqpagination.min.js"></script>

    <script>
        $(function () {
            $("#streamers").autocomplete({
                source: '${pageContext.request.contextPath}/autocompleteStreamerList',
            });

            $("#championPlayed").autocomplete({
                source: '${pageContext.request.contextPath}/autocompleteChampionList',
            });

            $("#championFaced").autocomplete({
                source: '${pageContext.request.contextPath}/autocompleteChampionList',
            });
        });

        $(document).ready(function () {
            $('.pagination').jqPagination({
                link_string: '/searchClips?p=',
                max_page: '${numClipPages}',
                paged: function (page) {
                    var map = {};
                    map['streamerName'] = '${fn:escapeXml(param.streamerName)}';
                    map['championPlayedString'] = '${fn:escapeXml(param.championPlayedString)}';
                    map['championFacedString'] = '${fn:escapeXml(param.championFacedString)}';
                    map['minLength'] = '${fn:escapeXml(param.minLength)}';
                    map['maxLength'] = '${fn:escapeXml(param.maxLength)}';
                    map['roleCriteria'] = '${roleCriteria}';
                    map['eloCriteria'] = '${eloCriteria}';
                    map['orderBy'] = '${orderBy}';
                    map['desc'] = '${desc}';
                    post_to_url(this.link_string + page, map);
                }
            });
        });
    </script>
</head>

<body>

<a href="/"><img class="logo" src="/static/images/logo.png"></a>

<form method="POST" action="/searchClips">
    <input type="hidden" name="orderBy" value="start_time">
    <input type="hidden" name="desc" value="true">
    <table class="search">
        <tr>
            <td class="randomSkin" rowspan="5"><img class="randomSkin" src=${randomSkinFile}></td>
            <td class="search">Streamer:</td>
            <td class="searchBox">
                <div class="ui-widget">
                    <input id="streamers" tabindex="1" type="text" name="streamerName"
                           value=${fn:escapeXml(param.streamerName)}>
                </div>
            </td>
            <td class="searchRole checkBox" rowspan="5">
                <c:set var="topChecked" value=""/>
                <c:set var="midChecked" value=""/>
                <c:set var="jungChecked" value=""/>
                <c:set var="adcChecked" value=""/>
                <c:set var="suppChecked" value=""/>

                <c:forEach items="${roleCriteria}" var="role">
                <c:choose>
                <c:when test="${role eq '\"TOP\"'}">
                    <c:set var="topChecked" value="checked"/>
                </c:when>
                <c:when test="${role eq '\"MID\"'}">
                    <c:set var="midChecked" value="checked"/>
                </c:when>
                <c:when test="${role eq '\"JUNG\"'}">
                    <c:set var="jungChecked" value="checked"/>
                </c:when>
                <c:when test="${role eq '\"ADC\"'}">
                    <c:set var="adcChecked" value="checked"/>
                </c:when>
                <c:when test="${role eq '\"SUPP\"'}">
                    <c:set var="suppChecked" value="checked"/>
                </c:when>
                </c:choose>
                </c:forEach>

                <input type="checkbox" id="checkboxTop" class="css-checkbox lrg" name="roleCriteria"
                       value="TOP" ${topChecked}>
                <label for="checkboxTop" class="css-label lrg web-two-style">Top</label><br>

                <input type="checkbox" id="checkboxMid" class="css-checkbox lrg" name="roleCriteria"
                       value="MID" ${midChecked}>
                <label for="checkboxMid" class="css-label lrg web-two-style">Mid</label><br>

                <input type="checkbox" id="checkboxJung" class="css-checkbox lrg" name="roleCriteria"
                       value="JUNG" ${jungChecked}>
                <label for="checkboxJung" class="css-label lrg web-two-style">Jung</label><br>

                <input type="checkbox" id="checkboxAdc" class="css-checkbox lrg" name="roleCriteria"
                       value="ADC" ${adcChecked}>
                <label for="checkboxAdc" class="css-label lrg web-two-style">ADC</label><br>

                <input type="checkbox" id="checkboxSupp" class="css-checkbox lrg" name="roleCriteria"
                       value="SUPP" ${suppChecked}>
                <label for="checkboxSupp" class="css-label lrg web-two-style">Supp</label>

            <td class="searchTier checkBox" rowspan="5">
                <c:set var="challengerChecked" value=""/>
                <c:set var="diamondChecked" value=""/>
                <c:set var="platinumChecked" value=""/>
                <c:set var="goldChecked" value=""/>
                <c:set var="silverChecked" value=""/>
                <c:set var="bronzeChecked" value=""/>

                <c:forEach items="${eloCriteria}" var="elo">
                <c:choose>
                <c:when test="${elo eq 'CHALLENGER'}">
                    <c:set var="challengerChecked" value="checked"/>
                </c:when>
                <c:when test="${elo eq 'DIAMOND'}">
                    <c:set var="diamondChecked" value="checked"/>
                </c:when>
                <c:when test="${elo eq 'PLATINUM'}">
                    <c:set var="platinumChecked" value="checked"/>
                </c:when>
                <c:when test="${elo eq 'GOLD'}">
                    <c:set var="goldChecked" value="checked"/>
                </c:when>
                <c:when test="${elo eq 'SILVER'}">
                    <c:set var="silverChecked" value="checked"/>
                </c:when>
                <c:when test="${elo eq 'BRONZE'}">
                    <c:set var="bronzeChecked" value="checked"/>
                </c:when>
                </c:choose>
                </c:forEach>
                <input type="checkbox" id="checkboxChallenger" class="css-checkbox lrg" name="eloCriteria"
                       value="CHALLENGER" ${challengerChecked}>
                <label for="checkboxChallenger" class="css-label lrg web-two-style">Challenger</label><br>

                <input type="checkbox" id="checkboxDiamond" class="css-checkbox lrg" name="eloCriteria"
                       value="DIAMOND" ${diamondChecked}>
                <label for="checkboxDiamond" class="css-label lrg web-two-style">Diamond</label><br>

                <input type="checkbox" id="checkboxPlatnium" class="css-checkbox lrg" name="eloCriteria"
                       value="PLATINUM" ${platinumChecked}>
                <label for="checkboxPlatnium" class="css-label lrg web-two-style">Platinum</label><br>

                <input type="checkbox" id="checkboxGold" class="css-checkbox lrg" name="eloCriteria"
                       value="GOLD" ${goldChecked}>
                <label for="checkboxGold" class="css-label lrg web-two-style">Gold</label><br>

                <input type="checkbox" id="checkboxSilver" class="css-checkbox lrg" name="eloCriteria"
                       value="SILVER" ${silverChecked}>
                <label for="checkboxSilver" class="css-label lrg web-two-style">Silver</label><br>

                <input type="checkbox" id="checkboxBronze" class="css-checkbox lrg" name="eloCriteria"
                       value="BRONZE" ${bronzeChecked}>
                <label for="checkboxBronze" class="css-label lrg web-two-style">Bronze</label>

            <td class="search" rowspan="5">
                <input class="searchButton" type="image"
                       src="/static/images/ui/buttons/button_search.png"
                       alt="Search clips">
            </td>
        </tr>
        <tr>
            <td class="search">Champion Played:</td>
            <td class="searchBox">
                <div class="ui-widget">
                    <input id="championPlayed" tabindex="2" type="text" name="championPlayedString"
                           value="${fn:escapeXml(param.championPlayedString)}">
                </div>
            </td>
        </tr>
        <tr>
            <td class="search">Champion Faced:</td>
            <td class="searchBox">
                <div class="ui-widget">
                    <input id="championFaced" tabindex="2" type="text" name="championFacedString"
                           value=${fn:escapeXml(param.championFacedString)}>
                </div>
            </td>
        </tr>
        <tr>
            <td class="search">Min Length:</td>
            <td class="searchBox">
                <div class="ui-widget">
                    <input id="minLength" tabindex="3" class="clipLength" type="number" name="minLength"
                           value=${fn:escapeXml(param.minLength)}>
                </div>
            </td>
        </tr>
        <tr>
            <td class="search">Max Length:</td>
            <td class="searchBox">
                <div class="ui-widget">
                    <input tabindex="4" class="clipLength" type="number" name="maxLength"
                           value=${fn:escapeXml(param.maxLength)}>
                </div>
            </td>
        </tr>
    </table>
</form>

<br>
<table class="clips">
<tr>
<th>Date
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="dateImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="dateImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'start_time'}">
            <c:if test="${desc == false}">
                <c:set var="dateImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="dateImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${dateImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${dateImageClass}" type="image" name="orderBy"
               value="start_time" src="${dateImage}">
    </form>
</th>
<th>Streamer
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="streamerImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="streamerImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'streamer_name'}">
            <c:if test="${desc == false}">
                <c:set var="streamerImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="streamerImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${streamerImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${streamerImageClass}" type="image" name="orderBy"
               value="streamer_name" src="${streamerImage}">
    </form>
</th>
<th>Role
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="roleImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="roleImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'role_played'}">
            <c:if test="${desc == false}">
                <c:set var="roleImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="roleImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${roleImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${roleImageClass}" type="image" name="orderBy"
               value="role_played" src="${roleImage}">
    </form>
</th>
<th colspan="2"> Matchup
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="championPlayedImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="championPlayedImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'champion_played'}">
            <c:if test="${desc == false}">
                <c:set var="championPlayedImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="championPlayedImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${championPlayedImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${championPlayedImageClass}" type="image" name="orderBy"
               value="champion_played" src="${championPlayedImage}">
    </form>
</th>
<th>Tier
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="tierImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="tierImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'elo'}">
            <c:if test="${desc == false}">
                <c:set var="tierImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="tierImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${tierImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${tierImageClass}" type="image" name="orderBy"
               value="elo" src="${tierImage}">
    </form>
</th>
<th>Length
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="lengthImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="lengthImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'length'}">
            <c:if test="${desc == false}">
                <c:set var="lengthImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="lengthImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${lengthImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${lengthImageClass}" type="image" name="orderBy"
               value="length" src="${lengthImage}">
    </form>
</th>
<th>Views
    <form method="POST" action="/searchClips">
        <input type="hidden" name="streamerName" value=${fn:escapeXml(param.streamerName)}>
        <input type="hidden" name="championPlayedString" value=${fn:escapeXml(param.championPlayedString)}>
        <input type="hidden" name="championFacedString" value=${fn:escapeXml(param.championFacedString)}>
        <input type="hidden" name="minLength" value=${fn:escapeXml(param.minLength)}>
        <input type="hidden" name="maxLength" value=${fn:escapeXml(param.maxLength)}>
        <c:forEach items="${roleCriteria}" var="role">
            <c:out value='<input type="hidden" name="roleCriteria" value=${role}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:forEach items="${eloCriteria}" var="elo">
            <c:out value='<input type="hidden" name="eloCriteria" value=${elo}>' escapeXml="false"></c:out>
        </c:forEach>
        <c:set var="viewsImage" value="/static/images/ui/buttons/button_sortDown.png"></c:set>
        <c:set var="viewsImageClass" value="sortButtonDisabled"></c:set>
        <c:if test="${orderBy == 'views'}">
            <c:if test="${desc == false}">
                <c:set var="viewsImage" value="/static/images/ui/buttons/button_sortUp.png"></c:set>
            </c:if>
            <c:set var="viewsImageClass" value="sortButtonEnabled"></c:set>
        </c:if>
        <c:choose>
            <c:when test="${viewsImageClass eq 'sortButtonEnabled' && desc == true}">
                <c:out value='<input type="hidden" name="desc" value="false">' escapeXml="false"></c:out>
            </c:when>
            <c:otherwise>
                <c:out value='<input type="hidden" name="desc" value="true">' escapeXml="false"></c:out>
            </c:otherwise>
        </c:choose>
        <input class="${viewsImageClass}" type="image" name="orderBy"
               value="views" src="${viewsImage}">
    </form>
</th>
<th>Rating</th>
<th class="paginationHeader">
    <div class="pagination">
        <a class="first" data-action="first">&laquo;</a>
        <a class="previous" data-action="previous">&lsaquo;</a>
        <input type="text" readonly="readonly" data-current-page="${param.p}" data-max-page="${numClipsPage}"/>
        <a class="next" data-action="next">&rsaquo;</a>
        <a class="last" data-action="last">&raquo;</a>
    </div>
</th>
</tr>

<c:forEach items="${clips}" var="clip" varStatus="status">
    <tr class="clips">
        <td><fmt:formatDate type="date" value="${clip.startTime}"/></td>
        <td>
            <form method="POST" id="streamerForm${status.index}" action="/searchClips">
                <input type='hidden' name='streamerName' value="${clip.streamerName}">
                <input type="hidden" name="orderBy" value="start_time">
                <input type="hidden" name="desc" value="true">
                <a href="javascript: submitForm('streamerForm${status.index}')">
                        ${clip.streamerName}</a>
            </form>
        </td>
        <td>
            <form method="POST" id="roleForm${status.index}" action="/searchClips">
                <input type='hidden' name='roleCriteria' value="${clip.rolePlayed.name}">
                <input type="hidden" name="orderBy" value="start_time">
                <input type="hidden" name="desc" value="true">
                <a href="javascript: submitForm('roleForm${status.index}')">
                        ${fn:toLowerCase(clip.rolePlayed.name)}</a>
            </form>
        </td>
        <td colspan="2">
            <form method="POST" id="champFormA${status.index}" action="/searchClips">
                <input type="hidden" name="orderBy" value="start_time">
                <input type="hidden" name="desc" value="true">
                <input class="avatarSelf" type='image' name='championPlayedString'
                       value="${fn:toLowerCase(clip.championPlayed.name)}"
                       src="static/images/avatars/avatar_${fn:toLowerCase(clip.championPlayed.name)}.png">
            </form>

            <!-- Note cannot use <a href> here because a small line appears after image for some reason -->
            <c:choose>
                <c:when test="${empty clip.lanePartnerChampion.name}">
                    <c:out value='<img class="avatar" src="static/images/avatars/avatar_blank.png">'
                           escapeXml="false"></c:out>
                </c:when>
                <c:otherwise>
                    <c:set var="lanePartnerChampionName" value="${fn:toLowerCase(clip.lanePartnerChampion.name)}"/>
                    <c:out value='<form method="POST" id="champFormB${status.index}" action="/searchClips">
                                      <input type="hidden" name="orderBy" value="start_time">
                                      <input type="hidden" name="desc" value="true">
                                      <input class="avatar" type="image" name="championPlayedString" value="${lanePartnerChampionName}"
                                             src="static/images/avatars/avatar_${lanePartnerChampionName}.png"></form>'
                           escapeXml="false">
                    </c:out>
                </c:otherwise>
            </c:choose>

            <img class="vs" src="/static/images/ui/vs.png"/>

            <form method="POST" id="champFormC${status.index}" action="/searchClips">
                <input type="hidden" name="orderBy" value="start_time">
                <input type="hidden" name="desc" value="true">
                <input class="avatarEnemy" type='image' name='championPlayedString'
                       value="${fn:toLowerCase(clip.championFaced.name)}"
                       src="static/images/avatars/avatar_${fn:toLowerCase(clip.championFaced.name)}.png">
            </form>

            <!-- Note cannot use <a href> here because a small line appears after image for some reason -->
            <c:choose>
                <c:when test="${empty clip.enemyLanePartnerChampion.name}">
                    <c:out value='<img class="avatar" src="static/images/avatars/avatar_blank.png">'
                           escapeXml="false"></c:out>
                </c:when>
                <c:otherwise>
                    <c:set var="enemyLanePartnerChampionName"
                           value="${fn:toLowerCase(clip.enemyLanePartnerChampion.name)}"/>
                    <c:out value='<form method="POST" id="champFormD${status.index}" action="/searchClips">
                                      <input type="hidden" name="orderBy" value="start_time">
                                      <input type="hidden" name="desc" value="true">
                                      <input class="avatar" type="image" name="championPlayedString" value="${enemyLanePartnerChampionName}"
                                             src="static/images/avatars/avatar_${enemyLanePartnerChampionName}.png"></form>'
                           escapeXml="false">
                    </c:out>
                </c:otherwise>
            </c:choose>

        </td>
        <td>
            <form method="POST" id="champFormC${status.index}" action="/searchClips">
                <input type="hidden" name="orderBy" value="start_time">
                <input type="hidden" name="desc" value="true">
                <input class="badgeChallenger" type='image' name='eloCriteria'
                       value="${clip.generalElo}"
                       src="static/images/badge/badge3_${fn:toLowerCase(clip.generalElo)}.png">
            </form>
        </td>
        <td><fmt:formatNumber value="${clip.length / 60}" maxFractionDigits="0"/> min</td>
        <td><fmt:formatNumber value="${clip.views}"/></td>
        <td>9.5</td>
        <td><a href="${clip.url}"><img class="watchButton"
                                       src="static/images/ui/buttons/button_watchVideo.png"/></a></td>
    </tr>
</c:forEach>

</table>
</form>
</body>

</html>