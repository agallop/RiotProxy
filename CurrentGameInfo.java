package hi.apitest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 5/4/2016.
 */
public class CurrentGameInfo extends LeagueData{
    public final List<BannedChampion> bannedChampions;
    public final long gameId;
    public final long gameLength;
    public final String gameMode;
    public final long gameQueueConfigId;
    public final long gameStartTime;
    public final String gameType;
    public final long mapId;
    public final Observer observers;
    public final List<CurrentGameParticipant> participants;
    public final String platformId;

    public CurrentGameInfo(JSONObject data){

        //Getting members from JsonObject
        bannedChampions = BannedChampion.getBannedChampions(data);
        gameId = getLong(data, "gameId");
        gameLength = getLong(data, "gameLength");
        gameMode = getString(data, "gameMode");
        gameQueueConfigId = getLong(data, "gameQueueConfigId");
        gameStartTime = getLong(data, "gameStartTime");
        gameType = getString(data, "gameType");
        mapId = getLong(data, "mapId");
        observers = Observer.getObserver(data);
        participants = CurrentGameParticipant.getParticipants(data);
        platformId = getString(data, "platformId");
    }

    public List<CurrentGameParticipant> getTeam(long teamId){
        ArrayList<CurrentGameParticipant> team = new ArrayList<CurrentGameParticipant>();
        for(CurrentGameParticipant participant : participants){
            if(participant.teamId == teamId){
                team.add(participant);
            }
        }
        return team;
    }

    public List<CurrentGameParticipant> getEnemyTeam(long teamId){
        ArrayList<CurrentGameParticipant> team = new ArrayList<CurrentGameParticipant>();
        for(CurrentGameParticipant participant : participants){
            if(participant.teamId != teamId){
                team.add(participant);
            }
        }
        return team;
    }

    public long getTeamId(long summonerId){
      for(CurrentGameParticipant participant : participants){
        if(participant.summonerId = summonerId)
          return participant.teamId;
      }

      return 0;
    }
}
