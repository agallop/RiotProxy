package hi.apitest;

import org.json.JSONObject;

/**
 * Created by Anthony on 4/26/2016.
 */
public class Summoner extends LeagueData{
    public final long id;
    public final String name;
    public final int  profileIconId;
    public final long revisionDate;
    public final long summonerLevel;

    //Makes a Summoner using primitives
    public Summoner(long id, String name, int profileIconId, long revisionDate, long summonerLevel){
        this.id = id;
        this.name = name;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    // Makes a Summoner given a JSON object
    public Summoner(JSONObject data) {
        id = getLong(data, "id");
        name = getString(data, "name");
        profileIconId = getInt(data, "profileIconId");
        revisionDate = getLong(data, "revisionDate");
        summonerLevel = getLong(data, "summonerLevel");
    }
}
