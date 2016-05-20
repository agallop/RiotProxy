package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 5/4/2016.
 */
public class BannedChampion extends LeagueData {
    public final long championId;
    public final int pickTurn;
    public final long teamId;

    public static List<BannedChampion> getBannedChampions(JSONObject data){
        List<BannedChampion> bannedChampions = new ArrayList<BannedChampion>();
        try{
            JSONArray dataArray = data.getJSONArray("bannedChampions");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                bannedChampions.add(new BannedChampion(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex){
        } finally {
            return bannedChampions;
        }
    }

    public BannedChampion(JSONObject data){
        championId = getLong(data, "championId");
        pickTurn = getInt(data, "pickTurn");
        teamId = getLong(data, "teamId");
    }



}
