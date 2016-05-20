package hi.apitest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 5/3/2016.
 */
public class Rune extends LeagueData{
    final int count;
    final long runeId;

    /* Returns a list of runes given JSONObject */
    public static List<Rune> getRunes(JSONObject data){
        List<Rune> runes = new ArrayList<Rune>();
        try{
            JSONArray dataArray = data.getJSONArray("runes");
            int size = dataArray.length();
            for(int i = 0; i < size; i++){
                runes.add(new Rune(dataArray.getJSONObject(i)));
            }
        } catch (Exception ex){
        } finally {
            return runes;
        }
    }

    /* Creates Rune using primitives */
    public Rune(int count, long runeId){
        this.count = count;
        this.runeId = runeId;
    }

    /* Creates Rune using JSONObject */
    public Rune(JSONObject data){
        count = getInt(data, "count");

        //get rank from data
        runeId = getLong(data, "runeId");
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(!(obj instanceof Rune))
            return false;
        if(((Rune) obj).runeId == this.runeId)
            return true;
        return false;
    }

}
